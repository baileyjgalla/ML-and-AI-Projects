import java.util.*;
import java.math.*;
import java.io.*;
import java.security.*;
import java.text.*;

/** Holds a RSA key, either public or private (or possibly both).  In
 * addition to *n*, which must be in all keys, one (or both) of *d*
 * and e* must be set.  Which one(s) will determine whether it's a
 * public key (if only *e* is set), a private key (if only *d* is
 * set), or a dual key (if both *d* and *e* are set). This class is
 * meant to hold the values, and has no methods itself.
 */
class RSAKey {

    /// The decryption key, only included in private keys.
    BigInteger d;

    /// The encryption key, only used in public keys.
    BigInteger e;

    /// The value *n*, which was computed by p*q during the key generation
    BigInteger n;

    /// The bit length of p and also q during the key generation; n is
    /// expected to be twice that value
    int bitLength;
}


/** Holds the necessary cipher text information.  This class is meant
 * to hold the values, and has no methods itself.
 */
class CipherText {

    /// The list of encrypted blocks in the cipher text
    ArrayList<BigInteger> encryptedBlocks;

    /// The length of the original plaintext message
    int fileLength;

    /// The length of each block, in characters
    int blockLength;

    /// Default constructor, it initializes an empty object.
    CipherText() {
        encryptedBlocks = new ArrayList<BigInteger>();
    }
}


/** The main RSA class.  This is the class that contains all of the
 * functionality, and there are five methods that need to be created
 * for this assignment: {@link generateKeys()}, {@link encrypt()},
 * {@link decrypt()}, {@link sign()}, {@link checkSign()}, and {@link
 * crack()}.
 */
public class RSA {

    /** Whether to print verbose messages.  This is useful for
     * debugging, as it will never be set to true (via the -verbose
     * flag) during grading.
     */
    static boolean verbose = false;

    /** Whether the values of p and q are displayed during key
     * generation.  This is useful for debugging, and it *is*
     * something that we will test during grading.
     */
    static boolean showPandQ = false;

    /** The hash algorithm to be used.  While there are many
     * available, we'll use SHA-256 for this code.
     */
    final static String hashAlgorithm = "SHA-256";

    /** Write a {@link RSAKey} key (public or private) to a file.
     *
     * @param key The {@link RSAKey} to write to the file
     * @param keyName The base of the file name.  If the base is
     * 'foo', then this file will write foo-public.key and/or
     * foo-private.key, as appropriate.
     *
     * @throws Exception If the file handling code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    static void writeKeyToFile (RSAKey key, String keyName) throws Exception {
        if ( key.e != null ) { // it's a public key
            PrintStream output = new PrintStream(new File(keyName+"-public.key"));
            output.println("public\n" + key.bitLength + "\n" + key.e + "\n" + key.n);
            output.close();
        }
        if ( key.d != null ) { // it's a private key
            PrintStream output = new PrintStream(new File(keyName+"-private.key"));
            output.println("private\n" + key.bitLength + "\n" + key.d + "\n" + key.n);
            output.close();
        }
    }
    

    /** Read a {@link RSAKey} key (public or private) to a file and
     * return it.
     *
     * @param filename The key file name; it is assumed to be the
     * correct format, such as that written by {@link
     * writeKeyToFile()}, as there is no real error checking on the
     * file format.
     *
     * @return The RSAKey read in from the file.
     *
     * @throws Exception If the file handling code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    static RSAKey readKeyFromFile (String filename) throws Exception {
        Scanner filein = new Scanner(new File(filename));
        RSAKey key = new RSAKey();
        boolean isPublic = filein.next().equals("public");
        key.bitLength = filein.nextInt();
        if ( isPublic )
            key.e = new BigInteger(filein.next());
        else
            key.d = new BigInteger(filein.next());
        key.n = new BigInteger(filein.next());
        return key;
    }

    /** Read in cipher text from the provided file.
     *
     * @param filename The file name of the cipher text to read in
     *
     * @return A {@link CipherText} object representing the read in
     * cipher text
     *
     * @throws Exception If the file handling code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    static CipherText readCipherTextFromFile (String filename) throws Exception {
        Scanner filein = new Scanner(new File(filename));
        CipherText c = new CipherText();
        c.fileLength = filein.nextInt();
        c.blockLength = filein.nextInt();
        while ( filein.hasNext() ) {
            String num = filein.next();
            if ( num != "" )
                c.encryptedBlocks.add(new BigInteger(num));
        }
        return c;
    }

    /** Write cipher text to a file.
     *
     *
     * @param filename The file name to write the cipher text to
     * @param cipherText The {@link CipherText} object that contains
     * all the data to be written to the file.
     *
     * @throws Exception If the file handling code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    static void writeCipherTextToFile (String filename, CipherText cipherText) throws Exception {
        PrintStream output = new PrintStream(new File(filename));
        output.println(cipherText.fileLength + " " + cipherText.blockLength);
        for ( BigInteger num: cipherText.encryptedBlocks )
            output.println(num);
        output.close();
    }

    /** Read in the contents of a file into a string.
     *
     * @param filename The file to read in
     *
     * @return A String containing the entire contents of the passed
     * file
     *
     * @throws Exception If the file handling code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    static String getFileContents (String filename) throws Exception {
        File file = new File(filename);
        long fileLength = file.length();
        FileInputStream fin = new FileInputStream(file);
        byte[] data = new byte[(int)fileLength];
        fin.read(data);
        fin.close();
        return new String(data);
    }

    /** Convert a hash to a more human-readable string
     *
     * @param hash The `byte` array that is returned by the
     * computation of the hash (via the digest() routine in
     * java.security.MessageDigest)
     *
     * @return A String containing the printable hexadecimal
     * representation of the hash
     */
    static String convertHash (byte hash[]) {
        int hashSize = 0;
        try {
            hashSize = MessageDigest.getInstance(hashAlgorithm).getDigestLength() * 2;
        } catch (Exception e) {
            System.out.println ("Your Java installation does not support the '" + hashAlgorithm +
                                "' hashing method; change that in RSA.java to continue.");
            System.exit(1);
        }
        char chash[] = new char[hashSize];
        for ( int i = 0; i < hashSize/2; i++ ) {
            int hashValue = hash[i];
            if ( hashValue < 0 )
                hashValue += 256;
            if ( hashValue/16 < 10 )
                chash[2*i] = (char) ('0' + hashValue/16);
            else
                chash[2*i] = (char) ('a' + hashValue/16 - 10);
            if ( hashValue%16 < 10 )
                chash[2*i+1] = (char) ('0' + hashValue%16);
            else
                chash[2*i+1] = (char) ('a' + hashValue%16 - 10);
        }
        return new String(chash);
    }

    /** Converting a block to a BigInteger
     * 
     * @param text The ASCII text to convert into a BigInteger.
     *
     * @return A BigInteger that represents the passed in text.
     */
    public static BigInteger convertFromASCII(String text) {

        StringBuilder ascii = new StringBuilder();

        // Convert each character to its ASCII value and append it to the string
        for (char currentCharacter : text.toCharArray()) {
            // Get the ASCII value of the character and pad for consistent length
            // ascii.append((int) asciis);
            ascii.append(String.format("%03d", (int) currentCharacter));
        }
        //ASCII string to BigInteger
        return new BigInteger(ascii.toString());
    }

    /** Converting a block to ASCII
     *
     * @param block The BigInteger block to convert into ASCII.
     *
     * @return A String that is the ASCII representation of the
     * BigInteger parameter.
     */
    public static String convertToASCII(BigInteger block) {
        StringBuilder sb = new StringBuilder();
        
        // makes the block bigInt into a string
        String digits = block.toString();

        int paddingLength = digits.length() % 3;
        if (paddingLength != 0) {
            // Pad so all ascii chars are converted to a 3 digit represenation
            digits = String.format("%0" + (digits.length() + (3 - paddingLength)) + "d", block);
        }

        // Convert 3 digits chunks to a character and append
        for (int i = 0; i < digits.length(); i += 3) {
            int asciiValue = Integer.parseInt(digits.substring(i, i + 3));
            sb.append((char) asciiValue);
        }
        return sb.toString();
    }

    /** RSA key generation
     *
     * @param bitLength The bit length of *p* and *q* used in the
     * generation routines; this means that *n* will be approximately
     * *2n* bits in length.
     * @param seed The seed to provide to the random number generator;
     * if zero, then no seed is provided when the random number
     * generator is created.
     *
     * @return The keys generated.  Note that in the returned {@link
     * RSAKey}, *both* *d* and *e* are set, which means it contains
     * the information for both public and private keys.
     *
     * @throws Exception It is not expected that this method will
     * throw an exception, but this is included in case a future
     * implementation does choose to do so.
     */
    public static RSAKey generateKeys (int bitLength, int seed) throws Exception {
            //Initialize the random number generator w/ seed
            SecureRandom random = new SecureRandom();
            random.setSeed(seed);
    
            // Generate primes p and q
            int primeBitLength = bitLength / 2;
            BigInteger p = BigInteger.probablePrime(primeBitLength, random);
            BigInteger q = BigInteger.probablePrime(primeBitLength, random);
    
            // n = p * q
            BigInteger n = p.multiply(q);
    
            // phi = (p - 1) * (q - 1)
            BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

            // prime e for exponent
            BigInteger e = new BigInteger(n.bitLength() - 1, 100, random);
    
            // Make sure is e is relatively prime
            while (!phi.gcd(e).equals(BigInteger.ONE)) {
                e = e.add(BigInteger.TWO); // Increment to next odd
            }
    
            // Finding private key d
            BigInteger d = e.modInverse(phi);
    
            // Create and return the RSAKey object
            RSAKey rsaKey = new RSAKey();
            rsaKey.n = n;
            rsaKey.e = e;
            rsaKey.d = d;
            rsaKey.bitLength = bitLength;
    
            return rsaKey;
    }

    /** Performs RSA encryption
     *
     * This method will perform RSA encryption on the passed plain
     * text using the passed *public* key, and return the cipher text.
     *
     * Note that the returned {@link CipherText} object must have all
     * three fields set: bitLength, blockLength, *and*
     * encryptedBlocks.
     *
     * @param key The public key to encrypt the plain text with
     * @param plainText The text to encrypt
     *
     * @return A properly initialized CipherText object containing the
     * encrypted message.
     *
     * @throws Exception It is not expected that this method will
     * throw an exception, but this is included in case a future
     * implementation does choose to do so.
     */
    public static CipherText encrypt(RSAKey key, String plainText) throws Exception {
        CipherText cipherText = new CipherText();

        // Block size is in bytes with some adjustments
        // Divide bit length by 8 to convert to bytes but multiply 8 by 3 since each ascii value has three characters
        //Subtract 1 to avoid overflow
        int blockSize = (key.n.bitLength() / (8 * 3)) - 1;
        ArrayList<BigInteger> encryptedBlocks = new ArrayList<>();
    
        for (int i = 0; i < plainText.length(); i += blockSize) {
            String block = plainText.substring(i, Math.min(i + blockSize, plainText.length()));
    
            // Change to decimal encoding of ascii, padded to 3 digits
            BigInteger blockValue = convertFromASCII(block);
            BigInteger encryptedBlock = blockValue.modPow(key.e, key.n);
    
            encryptedBlocks.add(encryptedBlock);
        }
        cipherText.encryptedBlocks = encryptedBlocks;
        cipherText.blockLength = blockSize;

        return cipherText;
    }
    

    /** Performs RSA decryption
     *
     * This method will perform RSA decryption on the passed cipher
     * text using the passed *private* key, and return the plain text.
     *
     * @param key The private key to encrypt the plain text with
     * @param cipherText a {@link CipherText} object containing the
     * cipher text to decrypt
     *
     * @return A String containing the decrypted message.
     *
     * @throws Exception It is not expected that this method will
     * throw an exception, but this is included in case a future
     * implementation does choose to do so.
     */
    public static String decrypt(RSAKey key, CipherText cipherText) throws Exception {
        StringBuilder decryptedText = new StringBuilder();

        for (BigInteger encryptedBlock : cipherText.encryptedBlocks) {
            // Decrypt each block
            BigInteger decryptedBlock = encryptedBlock.modPow(key.d, key.n);
    
            // Convert back to ASCII and append to string builder
            String blockText = convertToASCII(decryptedBlock);
            decryptedText.append(blockText);
        }
    
        return decryptedText.toString();
    }
        

    /** Performs RSA key cracking
     *
     * Using brute force cracking, will take a long time due to math behind RSA
     * Using it on a bit length of say 10 is reasonable to keep runtime low
     *
     * @param key The public key to crack
     *
     * @return A RSAKey containing the cracked *full* key (both d and
     * e are set, as well as the other fields)
     *
     * @throws Exception It is not expected that this method will
     * throw an exception, but this is included in case a future
     * implementation does choose to do so.
     */
     public static RSAKey crack (RSAKey key) throws Exception {

        // This is the n from the key
        BigInteger n = key.n;

        // This is the p that we will be updating to find q
        BigInteger p = BigInteger.TWO;

        // This is q
        BigInteger q = BigInteger.ZERO;

        //This is to end the while loop
        Boolean flag = false;

        // This is the loop that checks for q
        while(!flag){
            q = n.divide(p);
            flag = q.isProbablePrime(10);
            p = p.nextProbablePrime();
        }

        // Assures that p is correct
        p = n.divide(q);

        // Sets p and q to one less so that it can be used in the final calculation
        p = p.subtract(BigInteger.ONE);
        q = q.subtract(BigInteger.ONE);

        // This is to be used to find d, (p-1)(q-1)
        BigInteger pq = p.multiply(q);

        // Finding d
        BigInteger d = key.e.modInverse(pq);

        // This is the cracked RSAKey
        RSAKey cracked = new RSAKey();
        cracked.d = d;
        cracked.e = key.e;
        cracked.n = key.n;
        cracked.bitLength = key.bitLength;
        return cracked;
    }

    /** Performs RSA message signing
     *
     * Note that the particular hash algorithm to be used is set in
     * the {@link hashAlgorithm} variable, and is likely SHA-256.
     *
     * @param key The private key to sign the message with.
     * @param plainText the message to sign
     *
     * @return The cipher text containing the encrypted hash of the
     * message
     *
     * @throws Exception If the method digest code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    public static CipherText sign (RSAKey key, String plainText) throws Exception {
        // How to hash a message
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String hashedMessage = convertHash(digest.digest(plainText.getBytes()));

        // A new key with flipped d and e
        RSAKey key2 = new RSAKey();
        key2.e = key.d;
        key2.d = key.e;
        key2.n = key.n;
        key2.bitLength = key.bitLength;

        CipherText result = new CipherText();
        result = encrypt(key2, hashedMessage);
        // Return the encrypted hashed message
        return result;
    }

    /** Performs RSA message signature checking
     *
     * Note that the decrypt() method is likely expecting a private
     * key, so you will have to copy e over to d before passing it
     * into encrypt()
     *
     * Note that the particular hash algorithm to be used is set in
     * the {@link hashAlgorithm} variable, and is likely SHA-256.
     *
     * @param key The public key to sign the message with.
     * @param plainText The message to check the signature of
     * @param signature The encrypted signature to check
     *
     * @return True of false, depending if the signatures match (or
     * not)
     *
     * @throws Exception If the method digest code within the method
     * throws an exception, this method will pass that method up the
     * throw stack.
     */
    public static boolean checkSign (RSAKey key, String plainText, CipherText signature) throws Exception {

        // How to hash a message
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String hashedMessage = convertHash(digest.digest(plainText.getBytes()));

        // A new key with flipped d and e
        RSAKey key2 = new RSAKey();
        key2.e = key.d;
        key2.d = key.e;
        key2.n = key.n;
        key2.bitLength = key.bitLength;

        // Return the encrypted hashed message
        return hashedMessage.equals(decrypt(key2, signature));
    }

    /** Parses the parameters and allows program execution.
     *
     * The `main()` method will parse the command line parameters, and
     * call the appropriate methods within this class.  You should not
     * have to modify this main() method at all.
     *
     * The command line parameters are specified at {@link cmdparam}.
     *
     * @param args The command-line arguments
     *
     * @throws Exception Many of the invoked methods will pass up an
     * exception thrown by their file handling routines (or the
     * message digest routines).  The main method will pass that up as
     * well (which means program termination).
     */
    public static void main (String[] args) throws Exception {
        String outputFileName = "output.txt", inputFileName = "input.txt", keyName = "default";
        int seed = 0;
    
        for ( int i = 0; i < args.length; i++ ) {

            if ( args[i].equals("-verbose") )
                verbose = !verbose;

            else if ( args[i].equals("-output") )
                outputFileName = args[++i];

            else if ( args[i].equals("-input") )
                inputFileName = args[++i];

            else if ( args[i].equals("-key") )
                keyName = args[++i];

            else if ( args[i].equals("-showpandq") )
                showPandQ = true;

            else if ( args[i].equals("-keygen") ) {
                int bitLength = Integer.parseInt(args[++i]);
                RSAKey key = generateKeys(bitLength, seed);
                writeKeyToFile (key,keyName);
            }

            else if ( args[i].equals("-encrypt") ) {
                RSAKey key = readKeyFromFile (keyName + "-public.key");
                String plainText = getFileContents(inputFileName);
                CipherText cipherText = encrypt(key, plainText);
                writeCipherTextToFile(outputFileName, cipherText);
            }

            else if ( args[i].equals("-decrypt") ) {
                RSAKey key = readKeyFromFile (keyName + "-private.key");
                CipherText cipherText = readCipherTextFromFile(inputFileName);
                String plainText = decrypt(key, cipherText);

                System.out.println("Decrypt Text: " + plainText); // TODO: REMOVE ME

                PrintWriter fileout = new PrintWriter(outputFileName);
                fileout.print(plainText);
                fileout.close();
            }

            else if ( args[i].equals("-sign") ) {
                RSAKey key = readKeyFromFile (keyName + "-private.key");
                String plainText = getFileContents(inputFileName);
                CipherText signature = sign(key,plainText);
                writeCipherTextToFile(inputFileName+".sign", signature);
            }

            else if ( args[i].equals("-checksign") ) {
                RSAKey key = readKeyFromFile (keyName + "-public.key");
                String plainText = getFileContents(inputFileName);
                CipherText signature = readCipherTextFromFile(inputFileName+".sign");
                boolean result = checkSign(key,plainText,signature);
                if ( !result )
                    System.out.println("Signatures do not match!");
            }

            else if ( args[i].equals("-crack") ) {
                RSAKey key = readKeyFromFile (keyName + "-public.key");
                RSAKey crack = crack(key);
                writeKeyToFile (crack,keyName+"-cracked");
            }

            else if ( args[i].equals("-seed") )
                seed = Integer.parseInt(args[++i]);

            else {
                System.out.println("Unknown parameter: '" + args[i] + "', exiting.");
                System.exit(1);
            }
        }
    }
}

import java.util.Random;
import java.util.Scanner;

public class Des {
	// Initial Permutation (IP) table
	private static final int[] INITIAL_PERMUTATION = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

	// Final Permutation (FP) table
    private static final int[] FINAL_PERMUTATION = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    // Expansion (E) table
    private static final int[] EXPANSION_PERMUTATION = {
            32, 1, 2, 3, 4, 5, 4, 5,
            6, 7, 8, 9, 8, 9, 10, 11,
            12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21,
            22, 23, 24, 25, 24, 25, 26, 27,
            28, 29, 28, 29, 30, 31, 32, 1
    };
    //P Box table
    private static final int[] P_BOX = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25
    };
    // S-boxes
    private static final int[][][] S_BOXES = {
            {
                    // S1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    // S2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    // S3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    // S4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    // S5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    // S6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    // S7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    // S8
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };
    // Key Shift Table
    private static final int[] LEFT_SHIFT_SCHEDULE = {
            1, 1, 2, 2,
            2, 2, 2, 2,
            1, 2, 2, 2,
            2, 2, 2, 1
    };

    	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        
        String message;
        do {
            System.out.print("Enter the message to encrypt (8 characters only): ");
            message = scanner.nextLine();
            System.out.println();
          //check for the message length
            if (message.length() != 8) {
                System.out.println("Error: The message must have exactly 8 characters.");
                System.out.println();
            }
        } while (message.length() != 8);
        
        //Finding the Random Key
        Random random = new Random(); 
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            int bit = random.nextInt(2);
            keyBuilder.append(bit);
        }
        String key = keyBuilder.toString();

        System.out.println("Key: " + key);
        System.out.println("--- Encryption Steps ---");
        System.out.println();

        //Step � 1: 64-bit plain text block is given to Initial Permutation (IP) function.
        String binaryMessage = toBinaryString(message);
        int blockSize = 64;

        int numBlocks = (int) Math.ceil((double) binaryMessage.length() / blockSize);
        for (int blockIndex = 0; blockIndex < numBlocks; blockIndex++) {
            int startIndex = blockIndex * blockSize;
            int endIndex = Math.min(startIndex + blockSize, binaryMessage.length());
            String block = binaryMessage.substring(startIndex, endIndex);

            
            System.out.println("--- Block " + (blockIndex + 1) + " ---");
            System.out.println("Step � 1" );
            System.out.println("PlainText: " + block);
            
          //Step � 2: IP performed on 64-bit plain text block.
            String permutedBlock = applyInitialPermutation(block);
            System.out.println();
            System.out.println("Step � 2" );
            System.out.println("Initial Permutation (Binary): " + permutedBlock); 

            //Step � 3: IP produced two halves of the permuted block known as Left Plain Text (LPT) and Right Plain Text (RPT).

            String leftPlainText = permutedBlock.substring(0, 32);
            String rightPlainText = permutedBlock.substring(32);
            System.out.println();
            System.out.println("Step � 3" );
            System.out.println("Left Plain Text (Binary): " + leftPlainText);
            System.out.println("Right Plain Text (Binary): " + rightPlainText);
            
            //Step � 4: Each LPT and RPT performed 16-rounds of encryption process.
            System.out.println();
            System.out.println("Step � 4" );
            for (int round = 1; round <= 16; round++) {
                String previousLeft = leftPlainText;
                String previousRight = rightPlainText;

                leftPlainText = previousRight;
                rightPlainText = applyExpansionPermutation(previousRight);
                rightPlainText = xor(rightPlainText, key);
                rightPlainText = applySBoxes(rightPlainText);
                rightPlainText = applyPBox(rightPlainText);
                rightPlainText = xor(previousLeft, rightPlainText);

                System.out.println();
                System.out.println("--- Round " + round + " ---");
                System.out.println("Left Plain Text (Binary): " + leftPlainText);
                System.out.println("Right Plain Text (Binary): " + rightPlainText);
            }
            
            //Step � 5: LPT and RPT rejoined and Final Permutation (FP) is performed on combined block.
            String encryptedBlock = applyFinalPermutation(rightPlainText + leftPlainText);
            System.out.println();
            System.out.println("Step � 5" );
            System.out.println("Final Permutation (Binary): " + encryptedBlock);
            
            
            
            //Step � 6: 64-bit Cipher text block is generated.
            System.out.println();
            System.out.println("Step � 6" );
            String encryptedHex = toHexString(encryptedBlock);
            System.out.println("Cipher (Hex): " + encryptedHex);
            String encryptedMessage = binaryToString(encryptedBlock);
            System.out.println("Ciphertext: " + encryptedMessage);
          
            

            System.out.println();
            
            // Decryption Steps
            System.out.println();
            System.out.println("------------------- Decryption Steps ----------------------");
            System.out.println();

            String permutedEncryptedBlock = applyInitialPermutation(encryptedBlock);
            System.out.println("Initial Permutation (Binary): " + permutedEncryptedBlock);

            String leftCipherText = permutedEncryptedBlock.substring(0, 32);
            String rightCipherText = permutedEncryptedBlock.substring(32);
            System.out.println("Left Cipher Text (Binary): " + leftCipherText);
            System.out.println("Right Cipher Text (Binary): " + rightCipherText);

            int round = 16;
            while (round >= 1) {
                String previousLeft = leftCipherText;
                String previousRight = rightCipherText;

                leftCipherText = previousRight;
                rightCipherText = applyExpansionPermutation(previousRight);
                rightCipherText = xor(rightCipherText, key);
                rightCipherText = applySBoxes(rightCipherText);
                rightCipherText = applyPBox(rightCipherText);
                rightCipherText = xor(previousLeft, rightCipherText);

                System.out.println();
                System.out.println("--- Round " + round + " ---");
                System.out.println("Left Cipher Text (Binary): " + leftCipherText);
                System.out.println("Right Cipher Text (Binary): " + rightCipherText);

                round--;
            }

            String decryptedBlock = applyFinalPermutation(rightCipherText + leftCipherText);
            System.out.println();
            System.out.println("Final Permutation (Binary): " + decryptedBlock);

            String decryptedMessage = binaryToString(decryptedBlock);
            System.out.println("Plaintext: " + decryptedMessage);
            System.out.println();
          
       
        }

        scanner.close();
    }

    	// Transferring the plain text to binary
    private static String toBinaryString(String input) {
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (char c : input.toCharArray()) {
            String binary = Integer.toBinaryString(c);
            while (binary.length() < 8) {
                binary = "0" + binary;
            }
            binaryStringBuilder.append(binary);
        }
        return binaryStringBuilder.toString();
    }
  
    //Finding the Initial Permutation
    private static String applyInitialPermutation(String text) {
        StringBuilder permutedTextBuilder = new StringBuilder();
        for (int i : INITIAL_PERMUTATION) {
            permutedTextBuilder.append(text.charAt(i - 1));
        }
        return permutedTextBuilder.toString();
    }
    
    //Finding the Final Permutation
    private static String applyFinalPermutation(String text) {
        StringBuilder permutedTextBuilder = new StringBuilder();
        for (int i : FINAL_PERMUTATION) {
            permutedTextBuilder.append(text.charAt(i - 1));
        }
        return permutedTextBuilder.toString();
    }

    // Expansion permutation: Expand the 32-bit right plain text to 48 bits
    private static String applyExpansionPermutation(String text) {
        StringBuilder expandedTextBuilder = new StringBuilder();
        for (int i : EXPANSION_PERMUTATION) {
            expandedTextBuilder.append(text.charAt(i - 1));
        }
        return expandedTextBuilder.toString();
    }

    // S-box substitution: Perform S-box substitution on the 48-bit data
    private static String applySBoxes(String text) {
        StringBuilder resultBuilder = new StringBuilder();
        int startIndex = 0;
        int endIndex = 6;
        for (int i = 0; i < 8; i++) {
            String chunk = text.substring(startIndex, endIndex);
            int row = Integer.parseInt(chunk.charAt(0) + "" + chunk.charAt(5), 2);
            int col = Integer.parseInt(chunk.substring(1, 5), 2);
            int sBoxValue = S_BOXES[i][row][col];
            String binary = Integer.toBinaryString(sBoxValue);
            while (binary.length() < 4) {
                binary = "0" + binary;
            }
            resultBuilder.append(binary);
            startIndex = endIndex;
            endIndex += 6;
        }
        return resultBuilder.toString();
    }
 
    // P-box permutation: Perform permutation on the substituted data
    private static String applyPBox(String text) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i : P_BOX) {
            resultBuilder.append(text.charAt(i - 1));
        }
        return resultBuilder.toString();
    }

    // XOR and Swap: Perform XOR and swap between left and right halves
    private static String xor(String a, String b) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            char bitA = a.charAt(i);
            char bitB = b.charAt(i);
            resultBuilder.append(bitA ^ bitB);
        }
        return resultBuilder.toString();
    }
    // Converting the binary to Text
    public static String binaryToString(String binaryString) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = binaryString.length();

        // Process the binary string in chunks of 8 bits
        for (int i = 0; i < length; i += 8) {
            String chunk = binaryString.substring(i, Math.min(i + 8, length));
            int decimalValue = Integer.parseInt(chunk, 2);
            stringBuilder.append((char) decimalValue);
        }

        return stringBuilder.toString();
    }
    
    // Converting the binary to Text
    private static String toHexString(String binary) {
        StringBuilder hexBuilder = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            String chunk = binary.substring(i, i + 4);
            int decimal = Integer.parseInt(chunk, 2);
            String hex = Integer.toHexString(decimal).toUpperCase();
            hexBuilder.append(hex);
        }
        return hexBuilder.toString();
    }
    
    
}


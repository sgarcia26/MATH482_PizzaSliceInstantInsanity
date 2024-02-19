import java.util.Arrays;
import java.util.function.IntUnaryOperator;

public class PuzzleSolver {
    static int[][] puzzle;
    static int[][] colorCount;
    static int[] rotationCount;

    public static int p1(int i) {
        return 1 + (int) (i * 17 * Math.E) % 65;
    }

    static IntUnaryOperator[] puzzleFunctions = new IntUnaryOperator[]{PuzzleSolver::p1};
    
    public static void generate(int[][] puzzleMatrix, int puzzleNumber) {
        int[] count = new int[65];
        int colorIndex = 0;
        int layerNum = 0;

        while (layerNum < 65 && puzzleMatrix[64][2] == 0) {
            int j = 0;
            while (j < 3) {
                int n = puzzleFunctions[puzzleNumber].applyAsInt(colorIndex);
                if (count[n - 1] < 3) {
                    count[n - 1]++;
                    puzzleMatrix[layerNum][j] = n;
                    if (j == 2) {
                        layerNum++;
                    }
                    j++;
                }
                colorIndex++;
            }
        }
    }
    

    public static void rotateSlice(int[] slice, int numOfRotations) {
        for (int i = 0; i < numOfRotations; i++) {
            int temp = slice[0];
            slice[0] = slice[2];
            slice[2] = slice[1];
            slice[1] = temp;
        }
    }

    public static void addSlice(int[] slice) {
        for (int col = 0; col < 3; col++) {
            int colorNum = slice[col];
            colorCount[colorNum - 1][col] = 1;
        }
    }

    public static void removeSlice(int[] slice) {
        for (int col = 0; col < 3; col++) {
            int colorNum = slice[col];
            colorCount[colorNum - 1][col] = 0;
        }
    }

    public static boolean isSliceValid(int[] slice, int size) {
        for (int col = 0; col < 3; col++) {
            int colorNum = slice[col];
            if (colorCount[colorNum - 1][col] != 0) {
                return false;
            }
        }
        return true;
    }


    public static void updateCount(int[][] stack) {
        for (int s = 0; s < stack.length; s++) {
            for (int col = 0; col < 3; col++) {
                int colorNum = stack[s][col];
                colorCount[colorNum - 1][col] = 1;
            }
        }
    }

    public static boolean solve(int[][] stack) {
        int sliceIndex = 0;
        boolean backtrack = false;
        boolean noSolutionFlag = false;
    
        while (sliceIndex < stack.length) {
            if (sliceIndex < 0) {
                noSolutionFlag = true;
                break;
            }
            if (rotationCount[sliceIndex] <= 2) {
                if (backtrack) {
                    removeSlice(stack[sliceIndex]);
                    rotateSlice(stack[sliceIndex], 1);
                    rotationCount[sliceIndex]++;
                    if (rotationCount[sliceIndex] == 3) {
                        continue;
                    }
                }
    
                boolean valid = isSliceValid(stack[sliceIndex], stack.length);  // Pass size as an argument
                if (valid) {
                    addSlice(stack[sliceIndex]);
                    backtrack = false;
                    sliceIndex++;
                    continue;
                } else if (!valid && backtrack) {
                    backtrack = false;
                } else if (!backtrack) {
                    rotateSlice(stack[sliceIndex], 1);
                    rotationCount[sliceIndex]++;
                    continue;
                }
            } else if (rotationCount[sliceIndex] == 3) {
                rotationCount[sliceIndex] = 0;
                backtrack = true;
                sliceIndex--;
                continue;
            }
        }
    
        return !noSolutionFlag;
    }    

    public static boolean checkStack(int[][] stack) {
        // Initialize colorCount and rotationCount before using them
        colorCount = new int[65][3];  // Update the size to 65
        rotationCount = new int[stack.length];
    
        Arrays.fill(rotationCount, 0);
        Arrays.fill(colorCount[0], 0);
        return solve(stack);
    }

    public static int[][] createStack(int[] listOfSlices, int sizeOfPuzzle) {
        int[][] stack = new int[sizeOfPuzzle][3];
        for (int i = 0; i < sizeOfPuzzle; i++) {
            System.arraycopy(puzzle[listOfSlices[i] - 1], 0, stack[i], 0, 3);
        }
        return stack;
    }

    private static int combIndex = 0;

    public static int[][] getCombinations(int size) {
        int maxCombinations = 100000;  // Set a maximum limit for combinations
        int[][] comb = new int[maxCombinations][size];
        int[] temp = new int[size];
        combIndex = 0;  // Reset combIndex
        generateCombinations(comb, temp, 0, 1, size, maxCombinations);
        return Arrays.copyOf(comb, combIndex);
    }

    

    private static void generateCombinations(int[][] comb, int[] temp, int index, int start, int size, int maxCombinations) {
        if (index == size || combIndex >= maxCombinations) {
            if (combIndex < maxCombinations) {
                System.arraycopy(temp, 0, comb[combIndex], 0, size);
                combIndex++;
            }
            return;
        }
    
        for (int i = start; i <= 30 && 30 - i + 1 >= size - index; i++) {
            temp[index] = i;
            generateCombinations(comb, temp, index + 1, i + 1, size, maxCombinations);
        }
    }

    public static void main(String[] args) {
        for (int puzzleNum = 0; puzzleNum < 4; puzzleNum++) {
            puzzle = new int[65][3];  // Updated to use size 65
            generate(puzzle, puzzleNum);

            if (checkStack(puzzle)) {
                System.out.println("Full Stack Solution Found:");
                for (int[] row : puzzle) {
                    System.out.println(Arrays.toString(row));
                }
                System.out.println("------------------------------------------------");
            } else {
                System.out.println("No Solution Found for Full Stack. Finding Minimum Obstacle...");

                for (int size = 1; size < 65; size++) {  // Updated to use size 65
                    boolean minObstacle = false;
                    int solvableStacks = 0;
                    int[][] comb = getCombinations(size);
                    System.out.println("Checking for " + size + " Slices | Combinations: " + combIndex);
                    for (int[] listOfSlices : comb) {
                        int[][] stack = createStack(listOfSlices, size);
                        if (checkStack(stack)) {
                            solvableStacks++;
                        } else {
                            System.out.println("Unsolvable Stack found: " + Arrays.toString(listOfSlices));
                            for (int[] row : stack) {
                                System.out.println(Arrays.toString(row));
                            }
                            minObstacle = true;
                            break;
                        }
                    }

                    if (minObstacle) {
                        System.out.println("Minimum Obstacle: " + size);
                        System.out.println("------------------------------------------------");
                        break;
                    } else {
                        System.out.println(solvableStacks + "/" + combIndex + " Combinations with solution");
                    }
                }
            }
            combIndex = 0;
        }
    }
}
MATH 482 FALL 2023

Programming Project: Pizza Slice Instant Insanity puzzle

NAME: Sergio Garcia

Due: 12/12/2023

Part I

Using the generator 1 + (floor(i * 17 * e))mod 65, build and print out the puzzle of
size 65. Remember that in the raw generated sequence, we skip over colors that
have already occurred three times. Present the puzzle as a vertical list of triples.

I will provide a generator below that will create Instant Insanity-like puzzles constructed from right triangular prisms as described in class.  These “pie slices” are constrained to move only by rotation flipping.  They have colors on their (three) vertical sides.  There are only three positions that a given slice can have relative to the rest of the tower.  One can visualize the pie slice with a hole in the center through which a wire passes, preventing the flipping of any slice of the puzzle. If an obstacle exists then there is no solution.  As mentioned in class, by obstacle I refer to a subset of the pie slices, which cannot be part of any solution.  I.e., any stacking of these slices entails at least one of the three sides of the tower prism having some color showing up more than once.

When assigning colors to create this list, if a color generated would be the 4th occurrence of a particular color, skip this color and move on to the very next color generated.  This is to ensure that no color shows up more than 3 times in the puzzle color list.  

The first three colors will color the top slice, slice #1.  They will color in order the faces as seen from the top of the puzzle, clockwise.  The next three colors in the list will color the second slice, etc.  

Part II

If there is no solution, report “NO SOLUTION.” You need not determine a minimal
obstacle.

If there is a solution, write it out. Form the solution by rotating slices as necessary.

Part III

Although you are free to use any sources (except other groups), please list all
sources that you used. Please provide your source code for your solver. Briefly
describe the contribution of each group member. Also, have each group member
sign the hard copy under their contribution statement.


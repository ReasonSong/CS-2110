/* NetIds: rs2352, hy483. Time spent: hh hours, mm minutes. */

/** A collection of static String utility functions.
 * All methods assume that String parameters are non-null.
 *
 * If any method is called with arguments that do not satisfy the preconditions,
 * the behavior is undefined (it can do anything you want).  For example, you
 * could (but do not have to) use assert statements to test preconditions.
 */
public class A2 {
    /* Implementation notes:
     *
     * Wherever possible, prefer library functions to writing your own loops.
     *
     * The more complicated your loops become, the more important it is to explain
     * the logic in comments. We don't explain break and continue. We prefer that
     * you don't use them.
     *
     * When writing comments within a method body, you may find it useful to
     * use multi-line comments to describe WHAT the code doing, and single-line
     * comments to describe HOW it is doing it. For example:
     *
     *    /* Remove the unfrobnicatable whizbangs * /
     *    for (...)
     *        // NOTE: a whizbang is frobnicatable if it has two or more tails
     *        ...
     *
     *    /* Frobnicate all the remaining whizbangs * /
     *    for (...)
     *        ...
     */

    /* Notes on Java if statements and loops:
     *
     * Write an if statement like this:
     *
     *     if (boolean-expression) {
     *         do this if boolean-expression is true
     *     }
     *
     * Write an if-else statement like this:
     *
     *     if (boolean-expression) {
     *         do this if boolean-expression is true
     *     } else {
     *         do this if boolean-expression is false
     *     }
     *
     * Write a while-loop like this:
     *
     *    while ( boolean-expression ) {
     *        repetend (the loop body, "the thing to be repeated")
     *    }
     *
     * To execute the while-loop, do the following:
     *
     *   1. Evaluate the boolean-expression; if is is false, stop.
     *   2. Execute the repetend.
     *   3. Continue again at step 1.
     *
     * Write a for-loop to do-something several times, with i having values in
     * m..n-1 (i.e. with i being m, m+1, m+2, ..., n-1). 
     *
     *    for (int i= m; i < n; i= i+1) {
     *        repetend
     *    }
     *
     * The for-loop above is equivalent to the while-loop below, except that
     * local variable i is NOT usable after the loop; its scope is its assignment
     * together with the loop.
     *
     *    int i= m;
     *    while (i < n) {
     *        repetend
     *        i= i+1;
     *    }
     */

    /** Return either s1 + s2 or s1 - s2, depending on b.
     *  If b is true, return the sum, otherwise return the difference.
     */
    public static int sumDif(boolean b, int s1, int s2) {
        // This method is already implemented; it is here to give you something
        // to test, and to show you different ways of writing return statements.
        if (b) {
            int s;
            s = s1 + s2;
            return s;

            // equivalently:
            // int s = s1 + s2;
            // return s;

            // or simply:    return s1 + s2;
        }

        // b is false;
        return s1 - s2;
    }
    
	 private static int scanStart = 0;

    /** Return true if the first half of s is the same as the second half of s.
     *  Examples: For s = "" return true
     *            For s = "xxx" return false  (if the length is odd, it's false)
     *            For s = "xxxx" return true
     *            For s = "hellohello" return true
     *            For s = "helloworld" return false
     */
    public static boolean isDoubled(String s) {
        // TODO: There is no need for a loop. Do not use a loop.
        // Be sure to use s1.equals(s2) to test for equality of strings s1 and s2.
        // Do not use s1 == s2.
    	
    	assert s != null;
    	
    	if (s.length() == 0 || s.substring(s.length()/2, s.length()-1).equals(s.substring(0, s.length()/2-1)) ){
    		return true;
    	}
    	
    	return false;
    }

    /** Return s with its characters reversed.
     *  Examples: if s = "" return ""
     *            if s = "b" return "b"
     *            if s = "abc", return "cba"
     *            if s = "xxx", return "xxx"
     */
    public static String reverse(String s) {
        // TODO:
    	assert s != null;
    	
    	String reversed = "";
    	
    	for (int charNum = s.length(); charNum > 0; --charNum){
    		reversed += s.charAt(charNum - 1);
    	}
    	
        return reversed;
    }

    /** Return s but with each occurrence of a character in input replaced
     *  by the corresponding character in output.
     *  A character that does not appear in input is unchanged.
     *
     * Precondition: input and output are the same length.
     *               No character in input appears in output (otherwise,
     *               the idea of replacement might be ambiguous, depending
     *               on the order in which replacemens are done).
     *
     * Examples: encode("hello world", "", "")       = "hello world"
     *           encode("hello world", "abc", "lmn") = "hello world"
     *           encode("hello world", "hod", "xyz") = "xelly wyrlz"
     *           encode("hello world", "helowrd", ".......") = "..... ....."
     */
    public static String encode(String s, String input, String output) {
        // TODO This needs only ONE for-loop with a single statement in the loop
        //      body. Look for a suitable String method!
    	
    	assert s != null && input != null && output != null;
    	assert input.length() == output.length();
    	// another assert need here, maybe
    	for (int i = 0; i < input.length(); ++i) {
    		assert input.charAt(i) != output.charAt(i);
    	}
    	
    	String encoded = s;
    	for (int charNum = 0; charNum < input.length(); ++charNum){
    		encoded = encoded.replace(input.charAt(charNum), output.charAt(charNum));
    	}
    	
        return encoded;
    }

    /** Return the shortest substring x of s such that s = x + x + ⋯ + x.
     * Examples: For s = "" return ""
     *           For s = "xxxxxxxxx" return "x"
     *           For s = "xyxyxyxy" return "xy"
     *           For s = "hellohellohello" return "hello"
     *           For s = "hellohelloworld" return "hellohelloworld"
     *           For s = "hellohel" return "hellohel"
     */
    public static String deduplicate(String s) {
        //TODO This, no doubt, requires nested loops
    	assert s != null;
    	if (s.length() == 0) return s;
    	
    	String dedup = "" + s.charAt(0);	// The first shortest substring should be the first character
    	int charNum = 1;
    	int stringLength = s.length();
    	
    	
    	/** Scan every character in the string until the rest of the string is shorter
    	 *  than the current shortest substring.
    	 */
    	while (stringLength - charNum >= dedup.length()){	
    		
    		/** Check if the new character match the head of current shortest substring.
    		 *  Start from the second character.
    		 *  If doesn't match, add the character to the current shortest substring.
    		 *  If matches, continue check the rest. 
    		 */
    		if (s.charAt(charNum) != dedup.charAt(0)) {
    			
    			/** Add the current character to the current shortest substring.
    			 *  And check the next character
    			 */
    			dedup += s.charAt(charNum);
    			++ charNum;
    			
    		} else {	// Current char matches the first character of the string
    			
    			++ charNum;
    			int dedupLength = dedup.length() - 1;
    			int dedupCharNum = 1;
    			
    			/**	Check if the following string match the rest of the current shortest substring. 
    			 */
    			while(dedupLength > 0 && s.charAt(charNum) == dedup.charAt(dedupCharNum)){
    				++ charNum;
    				++ dedupCharNum;
    				-- dedupLength;
    			}
    			
    			/** If the rest length of the shortest substring is non-zero,
    			 *  which means the following string doesn't match,
    			 *  then make all the string down to the current character as the new shortest string.
    			 */
    			if (dedupLength != 0){
    				dedup = s.substring(0, charNum);
    				++ charNum;
    			}
    			
    			/** Check if there is any more character in the string.
    			 */
    			if (charNum == stringLength) {
    				return dedup;
    			}
    		}
    	}
    	
    	/** No shortest substring, return the whole string back.
    	 */
        return s;
    }

    /** Interpret a string as a formula and evaluate it.
     *
     * Formulas consist of a series of numbers separated by the
     * operators '+' or '-'.  Formulas may  contain space characters
     * between the numbers and the operators.
     *
     * Precondition: s is a valid formula and contains at least one number
     *
     * Examples: evaluate("3")             returns 3
     *           evaluate("3 + 4")         returns 7
     *           evaluate("100 - 25+50")   returns 125
     *           evaluate("9")             returns 9
     *           evaluate("   7   +   7   +    7  ") returns 21
     */
    
    /** Helper function to find the next non-space char in the string,
     *  return the char's index, or -1 if non-space char is not found.
     */
    public static int findNextNonSpace(String s, int startChar){
    	assert s.length() > startChar;
    	
    	int charNum = startChar;
    	while (charNum < s.length() && s.charAt(charNum) == ' '){
    		++ charNum;
    	}
    	if (charNum == s.length()) return -1;
    	return charNum;
    }
    
    /** Helper function to find the next space char in the string,
     *  return the index of ' ', or -1 if ' ' is not found.
     */
    public static int findNextSpace(String s ,int startChar){
    	assert s.length() > startChar;
    	
    	int charNum = startChar;
    	while (charNum < s.length() && s.charAt(charNum) != ' ' ){
    		++ charNum;
    	}
    	
    	if(charNum == s.length()) return -1;
    	
    	return charNum;
    }
    
    /** Helper function to find '+' or '-' in the string,
     *  return the index of '+' or '-'.
     */
    public static int findNextNonIntChar(String s ,int startChar){
    	assert s.length() > startChar;
    	
    	int charNum = startChar;
    	while (charNum < s.length() && Character.getNumericValue(s.charAt(charNum)) != -1 ){
    		++ charNum;
    	}
    	
    	if(charNum == s.length()) return charNum;
    	
    	return charNum;
    }
    
    /** Helper function to find integers in the string,
     *  return integer's value
     */
    public static int getNextInt(String s){
    	
    	int intStart = findNextNonSpace(s, scanStart);
    	assert intStart != -1 && s.charAt(intStart) != '0';
       	assert Character.getNumericValue(s.charAt(intStart)) != -1;
       	
    	int intEnd = findNextNonIntChar(s, intStart);
    	scanStart = intEnd;
    	
    	return Integer.parseInt(s.substring(intStart, intEnd));
    }
    
    public static int evaluate(String s) {
        // TODO You can use Integer.parseInt to convert a string
        // (like "12345") to the corresponding integer (12345).
    	
    	assert s != null && s.length() != 0;
    	assert findNextNonSpace(s, 0) != -1;
    	
    	scanStart = 0;
		int sum = getNextInt(s);
	
    	while (scanStart < s.length()){
    		
    		int nextNonSpaceNum = findNextNonSpace(s, scanStart);
    		if (nextNonSpaceNum == -1) return sum;	// Nothing but space left
    		
    		char operator = s.charAt(nextNonSpaceNum);
    		assert operator == '+' || operator == '-';
    		scanStart = nextNonSpaceNum + 1;
    		int nextInt = getNextInt(s);
    		
    		if (operator == '+') {
    			sum += nextInt;
    		} else {
    			sum -= nextInt;
    		}
    	}
    	
        return sum;
    }
}

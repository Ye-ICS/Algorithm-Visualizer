public class EditDistanceC {
    
    public static int computeEditDistance(String w1, String w2) {
        int m = w1.length();
        int n = w2.length();

        int[][] dp = new int[m + 1][n + 1];

        
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; 
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; 
        }

        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (w1.charAt(i - 1) == w2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; 
                } else {
                    int delete = dp[i - 1][j];      
                    int insert = dp[i][j - 1];      
                    int substitute = dp[i - 1][j - 1]; 

                    dp[i][j] = 1 + Math.min(delete, Math.min(insert, substitute));
                }
            }
        }

        return dp[m][n]; 
    }
}

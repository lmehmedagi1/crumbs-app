package ba.unsa.etf.rpr.tutorijal5;

public class Main {

    public static void knapsackDyProg(int[] W, int[] c, int w, int n) {
        int[][] dp = new int[n + 1][w + 1];
        int[][] tabela = new int[w + 1][n + 1];

        int v = 0;
        for (int i = 0; i <= w; ++i)
            tabela[i][0] = v++;

        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j <= w; ++j) {
                dp[i][j] = dp[i - 1][j];

                if ((j >= W[i - 1]) && (dp[i][j] < dp[i - 1][j - W[i - 1]] + c[i - 1]))
                    dp[i][j] = dp[i - 1][j - W[i - 1]] + c[i - 1];

                tabela[j][i] = dp[i][j];
            }
        }

        System.out.printf("%-10s\t", "v");
        for (int j = 1; j <= n; ++j)
            System.out.printf("%-10s\t", "z(v," + j + ")");

        System.out.println();
        for (int i = 0; i <= w; ++i) {
            boolean iste = true;
            for (int j = 1; j <= n; ++j) {
                if (i > 0 && tabela[i][j] != tabela[i - 1][j]) {
                    iste = false;
                    break;
                }
            }
            if (!iste || i == 0) {
                for (int j = 0; j <= n; ++j)
                    System.out.printf("%-10s\t", tabela[i][j]);
                System.out.print("\n");
            }
        }

        System.out.println("Max Value:\t" + dp[n][w]);
        System.out.println("Selected Packs: ");

        while (n != 0) {
            if (dp[n][w] != dp[n - 1][w]) {
                System.out.println("\tPackage " + n + " with W = " + W[n - 1] + " and Value = " + c[n - 1]);

                w = w - W[n - 1];
            }

            n--;
        }
    }

    public static void main(String[] args) {
        /*
         * Pack and Weight - Value
         */
        // (46, 30, 42, 30, 24, 26, 59, 56, 30, 38)
        // 256
        int[] w = new int[]{59, 50, 30, 37, 42, 43, 53, 20, 58, 30};
        int[] c = new int[]{59, 50, 30, 37, 42, 43, 53, 20, 58, 30};

        /*
         * Max Weight
         */
        int M = 263;
        int n = c.length;

        /*
         * Run the algorithm
         */
        knapsackDyProg(w, c, M, n);
    }
}

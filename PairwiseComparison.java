package ci.abidjan.rootoor;

public class PairwiseComparison {
	public static void main(String[] args) throws Exception {

		double[][] binaryData = {

				{ 1.0, 0.0, 0.0, 0.0, 0.0 }, 
				{ 1.0, 1.0, 1.0, 1.0, 0.0 }, 
				{ 0.0, 1.0, 1.0, 1.0, 1.0 },
				{ 1.0, 0.0, 1.0, 1.0, 1.0 }, 
				{ 0.0, 0.0, 1.0, 1.0, 1.0 }, 
				{ 1.0, 1.0, 1.0, 0.0, 0.0 },
				{ 0.0, 0.0, 1.0, 0.0, 1.0 } };

		double[] bin1 = { 1.0, 0.0, 0.0, 0.0, 0.0 };
		double[] bin2 = { 1.0, 1.0, 1.0, 1.0, 0.0 };
		double[] bin3 = { 0.0, 1.0, 1.0, 1.0, 1.0 };
		double[] bin4 = { 1.0, 0.0, 1.0, 1.0, 1.0 };
		double[] bin5 = { 0.0, 0.0, 1.0, 1.0, 1.0 };
		double[] bin6 = { 1.0, 1.0, 1.0, 0.0, 0.0 };
		double[] bin7 = { 0.0, 0.0, 1.0, 0.0, 1.0 };
		
		System.out.println("Tanimoto(1,1) = " + tanimoto(bin1, bin1));
		System.out.println("Tanimoto(1,2) = " + tanimoto(bin1, bin2));
		System.out.println("Tanimoto(1,3) = " + tanimoto(bin1, bin3));
		System.out.println("Tanimoto(1,4) = " + tanimoto(bin1, bin4));
		System.out.println("Tanimoto(1,5) = " + tanimoto(bin1, bin5));
		System.out.println("Tanimoto(1,6) = " + tanimoto(bin1, bin6));
		System.out.println("Tanimoto(1,7) = " + tanimoto(bin1, bin7));
		
		System.out.println("////////////////////////////////////");
		
		double[][] comparisonMatrix = tanimoto2(binaryData);
        printComparisonMatrix(comparisonMatrix);
		

	}// main

	public static double tanimoto(double[] vector1, double[] vector2) {

		double AB = 0.;
		double binaryEuclideanNormA = 0.;
		double binaryEuclideanNormB = 0.;
		double tanimotoSimilarity = 0.;

		AB = dotProduct(vector1, vector2);
		binaryEuclideanNormA = dotProduct(vector1, vector1);
		binaryEuclideanNormB = dotProduct(vector2, vector2);
		tanimotoSimilarity = AB / (binaryEuclideanNormA + binaryEuclideanNormB - AB);

		return tanimotoSimilarity;

	}

	/**
	 * Calculate the euclidean norm of a binary vector.
	 * 
	 * 
	 * 
	 * @param vector of binary data
	 * @return norm of the vector already in square.
	 */
	public static double binaryEuclideanNorm(double[] vector) {
		double norm = 0.;
		for (int i = 0; i < vector.length; i++) {
			// As values are binary, so it is not necessary to do : norm +=
			// sqrt(Math.pow(vector[i],2)
			norm += vector[i];
		}
		return norm;
	}

	/**
	 * Calculate the euclidean norm of a binary vector.
	 * 
	 * @param vector of binary data
	 * @return norm of the vector
	 */
	public static double dotProduct(double[] vector1, double[] vector2) {

		double dotProduct = 0.;

		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("Vectors must have the same length.");
		}

		for (int i = 0; i < vector1.length; i++) {
			dotProduct += vector1[i] * vector2[i];
		}
		return dotProduct;
	}
	
	public static double[][] tanimoto2(double[][] data) {
		int n = data.length;
		double[][] result = new double[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				double[] vector1 = data[i];
				double[] vector2 = data[j];
				double tanimoto = 0.;
				tanimoto = tanimoto(vector1, vector2);
				result[i][j] = tanimoto;
				result[j][i] = tanimoto; // Since Tanimoto(A, B) = Tanimoto(B, A)
			}
		}
		
		return result;
	}
	public static double[][] tanimoto(double[][] data) {
        int n = data.length;
        double[][] result = new double[n][n];
        
        // pairwise comparison
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {  // warning here  :  loop initialization must be int j = i;
                double[] vector1 = data[i]; // extracting vector1
                double[] vector2 = data[j]; // extracting vector2

                double intersection = 0;
                double union = 0;
                for (int k = 0; k < vector1.length; k++) {
                    if (vector1[k] == 1.0 && vector2[k] == 1.0) {
                        intersection++;
                    }
                    if (vector1[k] == 1.0 || vector2[k] == 1.0) {
                        union++;
                    }
                }
                double tanimoto = intersection / union;
                result[i][j] = tanimoto;
                result[j][i] = tanimoto; // Since Tanimoto(A, B) = Tanimoto(B, A)
            }
        }

        return result;
    }

    public static void printComparisonMatrix(double[][] matrix) {
        int n = matrix.length;

        // Print column indices
        System.out.print("\t");
        for (int i = 0; i < n; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.print(i + "\t"); // Print row index
            for (int j = 0; j < n; j++) {
                System.out.printf("%.2f\t", matrix[i][j]);
            }
            System.out.println();
        }
    }

}// class

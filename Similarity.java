package ci.abidjan.adi.core;

public class Similarity {

	/**
	 * Default Mandatory constructor
	 */
	public Similarity() {
	}

	/**
	 * Method. Computes the similarity index between a binary data Matrix A and a
	 * binary data Vector B with the possiblity to choose the index by specifying
	 * the index name.
	 * 
	 * Index Formulas name are : 
	 * - empty or null (default is jaccard), 
	 * - ja or jaccard,
	 * - sma or simple_matching,
	 * - sokal_michener,
	 * - tanimoto ,
	 * - jaccard_tanimoto ,
	 * - dice1,
	 * - dice2
	 * - sw_jaccard,
	 * - kulczynski1,
	 * - kulczynski2,
	 * - sokal_sneath1,
	 * - russel_rao,
	 * - rogers_tanimoto,
	 * - gower_legendre,
	 * - gower,
	 * - ochiai,
	 * - ct1 or consonni_todeschini1 
	 * - ct2 or consonni_todeschini2 
	 * - ct3 or consonni_todeschini3 
	 * - ct4 or consonni_todeschini4 
	 * 
	 * @param A
	 * @param B
	 * @param formulaName
	 * @return
	 */
	public static double[] advancedSimilaritySearch(Matrix A, Matrix B, String formulaName) {
		double[] a = A.ikjalgorithm1D(B).getArray(); // A*B
		double[] b = A.ikjalgorithm1D(B.scalar_minus_matrix(1)).getArray(); // A*(1-B)
		double[] c = A.scalar_minus_matrix(1).ikjalgorithm1D(B).getArray(); // (1-A)*B
		double[] d = A.scalar_minus_matrix(1).ikjalgorithm1D(B.scalar_minus_matrix(1)).getArray(); // (1-A)*(1-B)

		int sa = a.length; // size of a
		int sb = b.length; // size of b
		int sc = c.length; // size of c
		int sd = d.length; // size of d
		int size = sa + sb + sc + sd;

		// merging

		double[] merged_into_1D = merge(a, b, c, d); // fonction de fusion

		// transformer en 2D
		int mr = 4; // les 4 coefficients forment les lignes du nouveau tableau 2D appelé
		// fusionArray
		int nc = size / mr; // déduction du nombre de colonnes de fusion[]
		double[][] fusion = new double[mr][nc]; // row-wise (les 4 coefficients sur les lignes et les objets en
		// colonnes)
		// double[][] fusion2 = new double[nc][mr]; // col-wise (l'inverse de row-wise)

		double n11 = 0.0000;
		double n10 = 0.0000;
		double n01 = 0.0000;
		double n00 = 0.0000;

		double indice = 0.0000; // l'indice calculé
		// stocker les différents indices calculés dans un tableau 1D de
		// taille (mr=1*nc=nc)
		double[] indicesArray1D = new double[nc];

		for (int j = 0; j < nc; j++) { // row-wise
			for (int i = 0; i < mr; i++) {
				fusion[i][j] = merged_into_1D[i * nc + j];

				// les coefficients
				n11 = fusion[0][j];
				n10 = fusion[1][j];
				n01 = fusion[2][j];
				n00 = fusion[3][j];

				// l'indice selon la formule choisie
				// indice = n11/(n11+n10+n01);
				indice = computesimilarity(formulaName, n11, n10, n01, n00); // calcul de l'indice
				indicesArray1D[j] = indice; // stocker le calcul dans le tableau des indices
			}
		}
		return indicesArray1D;
	}

	/**
	 * Méthode. Calcule la similarité entre deux ojets (tableaux 1D, vecteurs) de
	 * type binaire et de même taille.
	 * 
	 * @param formulaName
	 * @param n11
	 * @param n10
	 * @param n01
	 * @param n00
	 * @return result l'indice calculé entre les deux objets
	 */
	private static double computesimilarity(String formulaName, double n11, double n10, double n01, double n00) {
		/*
		 * un switch ne fonctionne qu’avec quatre types de données et leurs wrappers,
		 * ainsi qu’avec le type enum et la classe String :
		 * 
		 * - byte et Byte - short et Short - int et Integer - char et Character - enum -
		 * String
		 * 
		 *******************************************************************************************
		 * 
		 * based on
		 * "https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0247751" *
		 * A comparison of 71 binary similarity coefficients: The effect of base rates *
		 *******************************************************************************************
		 * 
		 * Appendix: 71 binary similarity coefficients Some general definitions used in
		 * the presentation of the 71 binary similarity coefficients indices are n = a +
		 * b + c + d), τ1 = (max{a, b} + max{c, d} + max(a, c} + max{b, d}), τ2 = (max{a
		 * + c, b + d} + max{a + b, c + d}), N = n(n-1)/2, B = ab+cd, C = ac + bd, D =
		 * ad + bc, A = N–B–C–D, and π1 ≤ π2 are the base rates for the two binary
		 * random variables. The first 18 measures are co-occurrence coefficients that
		 * do not consider negative matches, d (we note that coefficients that include
		 * n, by definition, include a, b, c, and d). Coefficients A.19 and A.20 involve
		 * the ratio of a to n. Coefficients A.21 to A.30 have total matches (i.e., a+d)
		 * in the numerator. The next 16 coefficients (A.31 to A.46) involve some form
		 * of ad–bc in the numerator term. Coefficients A.47 to A.49 are related
		 * association coefficients that involve ad and/or bc products. Coefficients
		 * A.50 to A.51 are, respectively, the Rand index and adjusted Rand index, which
		 * are enormously popular in the clustering literature. Coefficient A.52 is
		 * Loevinger’s H, which is popular in Mokken scaling applications. The remaining
		 * 19 coefficients are assorted co-occurrence measures
		 */

		double result = 0.0000;
		double numerator = 0.0000;
		double denominator = 0.0000;

		double n = n11 + n10 + n01 + n00;
//		double N = n * (n - 1) / 2;
//		double B = n11 * n10 + n01 * n00;
//		double C = n11 * n01 + n10 * n00;
//		double D = n11 * n00 + n10 * n01;
//		double A = (N - B - C - D);

		switch (formulaName) {
		case "":// simple matching
			numerator = n11;
			denominator = n;
			result = numerator / denominator;
			break;
		case "simple_matching": // simple matching
			numerator = n11 + n00;
			denominator = n;
			result = numerator / denominator;
			break;
		case "sokal_michener": // simple matching
			numerator = n11 + n00;
			denominator = n;
			result = numerator / denominator;
			break;
		case "jaccard":
			numerator = n11;
			denominator = n11 + n10 + n01;
			result = numerator / denominator;
			break;
		case "tanimoto":
			numerator = n11;
			denominator = n11 + n10 + n01;
			result = numerator / denominator;
			break;
		case "jaccard_tanimoto":
			numerator = n11;
			denominator = n11 + n10 + n01;
			result = numerator / denominator;
			break;
		case "dice1":
			numerator = n11;
			denominator = n11 + n10;
			result = numerator / denominator;
			break;
		case "dice2":
			numerator = n11;
			denominator = n11 + n01;
			result = numerator / denominator;
			break;
		case "sw_jaccard":
			numerator = 3 * n11;
			denominator = 3 * n11 + n10 + n01;
			result = numerator / denominator;
			break;
		case "kulczynski1":
			numerator = n11;
			denominator = n10 + n01;
			result = numerator / denominator;
			break;
		case "kulczynski2":
			numerator = n11;
			double denominator1 = n11 + n10;
			double denominator2 = n11 + n01;
			result = 0.5 * ((numerator / denominator1) + (numerator / denominator2));
			break;
		case "sokal_sneath1":
			result = n11 / (n11 + 2 * n10 + 2 * n01);
			break;
		case "russel_rao":
			result = n11 / (n11 + n10 + n01 + n00);
			break;
		case "rogers_tanimoto":
			result = (n11 + n00) / (n11 + 2 * n10 + 2 * n01 + n00);
			break;
		case "consonni_todeschini1":
			result = Math.log10(1 + n11 + n10) / Math.log10(1 + n11 + n10 + n01);
			break;
		case "gower_legendre":
			result = (n11 + n00) / (n11 + 0.5 * (n10 + n01) + n00);
			break;
		case "gower":
			result = (n11 + n00) / Math.sqrt((n11 + n10) * (n11 + n01) * (n10 + n00) * (n01 + n00));
			break;
		case "ochiai":
			result = (n11 + n00) / Math.sqrt((n11 + n10) * (n11 + n01) * (n10 + n00) * (n01 + n00));
			break;
		case "consonni_todeschini2":
			result = Math.log10(1 + n11 + n00) / Math.log10(1 + n11 + n10 + n01 + n00);
			break;

		case "consonni_todeschiniII": // refactoring
			result = (Math.log10(1 + n) - Math.log10(1 + n10 + n01)) / Math.log10(1 + n);
			break;

		case "anderberg": // refactoring
			double tau1 = (Math.max(n11, n10) + Math.max(n01, n00) + Math.max(n11, n01) + Math.max(n10, n00));
			double tau2 = (Math.max(n11 + n01, n10 + n00) + Math.max(n11 + n10, n01 + n00));
			result = (tau1 - tau2) / (2 * n);
			break;

		case "dispersion": // refactoring
			result = (n11 * n00 - n10 * n01) / Math.pow(n, 2);
			break;

		default:
			// simple matching
			result = n11 / (n11 + n10 + n01 + n00);
			break;
		}// switch
		return result;
	}

	/**
	 * Method. Merges many 1D-arrays into one.
	 * 
	 * @param arrays
	 * @return
	 */
	private static double[] merge(double[]... arrays) {
		int length = 0;
		for (double[] array : arrays)
			length += array.length;

		double[] merged_into_1D = new double[length];

		int pos = 0;
		for (double[] array : arrays) {
			System.arraycopy(array, 0, merged_into_1D, pos, array.length);
			pos += array.length;
		}

		return merged_into_1D;
	}

}// class

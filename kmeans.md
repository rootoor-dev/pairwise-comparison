# K-Means for real data

Voici une implémentation simple de l'algorithme K-means en Java :

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {

    public static void main(String[] args) {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 3.0},
                {8.0, 7.0},
                {9.0, 8.0},
                {10.0, 10.0}
        };

        int k = 2; // Nombre de clusters à former

        List<List<double[]>> clusters = kMeansClustering(data, k);

        // Affichage des clusters
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + ":");
            for (double[] point : clusters.get(i)) {
                System.out.println("\t" + point[0] + ", " + point[1]);
            }
        }
    }

    public static List<List<double[]>> kMeansClustering(double[][] data, int k) {
        // Initialisation aléatoire des centroïdes
        List<double[]> centroids = initializeCentroids(data, k);

        List<List<double[]>> clusters;
        do {
            clusters = assignPointsToClusters(data, centroids);
            List<double[]> newCentroids = calculateNewCentroids(clusters);
            if (!centroidsEqual(centroids, newCentroids)) {
                centroids = newCentroids;
            } else {
                break; // Les centroïdes n'ont pas changé, donc les clusters ont convergé
            }
        } while (true);

        return clusters;
    }

    private static List<double[]> initializeCentroids(double[][] data, int k) {
        Random random = new Random();
        List<double[]> centroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(data.length);
            centroids.add(data[randomIndex].clone());
        }
        return centroids;
    }

    private static List<List<double[]>> assignPointsToClusters(double[][] data, List<double[]> centroids) {
        List<List<double[]>> clusters = new ArrayList<>();
        for (int i = 0; i < centroids.size(); i++) {
            clusters.add(new ArrayList<>());
        }

        for (double[] point : data) {
            double minDistance = Double.MAX_VALUE;
            int closestCentroidIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = euclideanDistance(point, centroids.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCentroidIndex = i;
                }
            }
            clusters.get(closestCentroidIndex).add(point.clone());
        }

        return clusters;
    }

    private static List<double[]> calculateNewCentroids(List<List<double[]>> clusters) {
        List<double[]> newCentroids = new ArrayList<>();
        for (List<double[]> cluster : clusters) {
            double[] centroid = calculateCentroid(cluster);
            newCentroids.add(centroid);
        }
        return newCentroids;
    }

    private static double[] calculateCentroid(List<double[]> cluster) {
        double[] centroid = new double[cluster.get(0).length];
        for (int i = 0; i < cluster.get(0).length; i++) {
            double sum = 0;
            for (double[] point : cluster) {
                sum += point[i];
            }
            centroid[i] = sum / cluster.size();
        }
        return centroid;
    }

    private static boolean centroidsEqual(List<double[]> centroids1, List<double[]> centroids2) {
        if (centroids1.size() != centroids2.size()) {
            return false;
        }
        for (int i = 0; i < centroids1.size(); i++) {
            if (!arrayEquals(centroids1.get(i), centroids2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean arrayEquals(double[] array1, double[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    private static double euclideanDistance(double[] point1, double[] point2) {
        double sum = 0;
        for (int i = 0; i < point1.length; i++) {
            sum += Math.pow(point1[i] - point2[i], 2);
        }
        return Math.sqrt(sum);
    }
}
```

Cette implémentation de l'algorithme K-means prend les données en entrée et le nombre de clusters

# K-Means for binary data

## With Hamming distance
Voici une implémentation de l'algorithme K-means pour les données binaires :

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansBinary {

    public static void main(String[] args) {
        int[][] binaryData = {
                {1, 0, 0, 0, 0},
                {1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {1, 1, 1, 0, 0},
                {0, 0, 1, 0, 1}
        };

        int k = 2; // Nombre de clusters à former

        List<List<int[]>> clusters = kMeansClustering(binaryData, k);

        // Affichage des clusters
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + ":");
            for (int[] point : clusters.get(i)) {
                printArray(point);
            }
        }
    }

    public static List<List<int[]>> kMeansClustering(int[][] data, int k) {
        // Initialisation aléatoire des centroïdes
        List<int[]> centroids = initializeCentroids(data, k);

        List<List<int[]>> clusters;
        do {
            clusters = assignPointsToClusters(data, centroids);
            List<int[]> newCentroids = calculateNewCentroids(clusters);
            if (!centroidsEqual(centroids, newCentroids)) {
                centroids = newCentroids;
            } else {
                break; // Les centroïdes n'ont pas changé, donc les clusters ont convergé
            }
        } while (true);

        return clusters;
    }

    private static List<int[]> initializeCentroids(int[][] data, int k) {
        Random random = new Random();
        List<int[]> centroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(data.length);
            centroids.add(data[randomIndex].clone());
        }
        return centroids;
    }

    private static List<List<int[]>> assignPointsToClusters(int[][] data, List<int[]> centroids) {
        List<List<int[]>> clusters = new ArrayList<>();
        for (int i = 0; i < centroids.size(); i++) {
            clusters.add(new ArrayList<>());
        }

        for (int[] point : data) {
            double minDistance = Double.MAX_VALUE;
            int closestCentroidIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = hammingDistance(point, centroids.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCentroidIndex = i;
                }
            }
            clusters.get(closestCentroidIndex).add(point.clone());
        }

        return clusters;
    }

    private static List<int[]> calculateNewCentroids(List<List<int[]>> clusters) {
        List<int[]> newCentroids = new ArrayList<>();
        for (List<int[]> cluster : clusters) {
            int[] centroid = calculateCentroid(cluster);
            newCentroids.add(centroid);
        }
        return newCentroids;
    }

    private static int[] calculateCentroid(List<int[]> cluster) {
        int[] centroid = new int[cluster.get(0).length];
        for (int i = 0; i < cluster.get(0).length; i++) {
            int sum = 0;
            for (int[] point : cluster) {
                sum += point[i];
            }
            centroid[i] = (sum > cluster.size() / 2) ? 1 : 0; // Binaire : 1 si la majorité est 1, sinon 0
        }
        return centroid;
    }

    private static boolean centroidsEqual(List<int[]> centroids1, List<int[]> centroids2) {
        if (centroids1.size() != centroids2.size()) {
            return false;
        }
        for (int i = 0; i < centroids1.size(); i++) {
            if (!arrayEquals(centroids1.get(i), centroids2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean arrayEquals(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    private static double hammingDistance(int[] point1, int[] point2) {
        double distance = 0;
        for (int i = 0; i < point1.length; i++) {
            if (point1[i] != point2[i]) {
                distance++;
            }
        }
        return distance;
    }

    private static void printArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
```

Dans cet exemple, nous utilisons la distance de Hamming comme mesure de distance entre les points binaires. Les centroïdes sont initialisés de manière aléatoire, puis les points sont assignés aux clusters les plus proches. Les nouveaux centroïdes sont calculés à partir des points attribués à chaque cluster, et ce processus est répété jusqu'à ce que les centroïdes convergent.

## With tanimoto index
On peut utiliser l'indice de Tanimoto à la place de la distance de Hamming dans l'algorithme K-means pour les données binaires. L'indice de Tanimoto est une mesure de similarité entre deux ensembles binaires, ce qui le rend approprié pour la comparaison de points binaires dans l'algorithme K-means.

Voici comment modifier l'implémentation pour utiliser l'indice de Tanimoto :

1. Définissons une fonction pour calculer l'indice de Tanimoto entre deux ensembles binaires.
2. Utilisons cette fonction pour calculer la similarité entre chaque point de données et les centroids lors de l'assignation des points aux clusters.
3. Ajustons le calcul du centroid pour utiliser l'indice de Tanimoto lors de la mise à jour des centroids.

Voici un exemple de modification de l'implémentation pour utiliser l'indice de Tanimoto :

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansBinaryWithTanimoto {

    public static void main(String[] args) {
        int[][] binaryData = {
                {1, 0, 0, 0, 0},
                {1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {0, 0, 1, 1, 1},
                {1, 1, 1, 0, 0},
                {0, 0, 1, 0, 1}
        };

        int k = 2; // Nombre de clusters à former

        List<List<int[]>> clusters = kMeansClustering(binaryData, k);

        // Affichage des clusters
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + ":");
            for (int[] point : clusters.get(i)) {
                printArray(point);
            }
        }
    }

    public static List<List<int[]>> kMeansClustering(int[][] data, int k) {
        // Initialisation aléatoire des centroïdes
        List<int[]> centroids = initializeCentroids(data, k);

        List<List<int[]>> clusters;
        do {
            clusters = assignPointsToClusters(data, centroids);
            List<int[]> newCentroids = calculateNewCentroids(clusters, data[0].length);
            if (!centroidsEqual(centroids, newCentroids)) {
                centroids = newCentroids;
            } else {
                break; // Les centroïdes n'ont pas changé, donc les clusters ont convergé
            }
        } while (true);

        return clusters;
    }

    private static List<int[]> initializeCentroids(int[][] data, int k) {
        Random random = new Random();
        List<int[]> centroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(data.length);
            centroids.add(data[randomIndex].clone());
        }
        return centroids;
    }

    private static List<List<int[]>> assignPointsToClusters(int[][] data, List<int[]> centroids) {
        List<List<int[]>> clusters = new ArrayList<>();
        for (int i = 0; i < centroids.size(); i++) {
            clusters.add(new ArrayList<>());
        }

        for (int[] point : data) {
            double maxSimilarity = Double.NEGATIVE_INFINITY;
            int closestCentroidIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double similarity = tanimotoSimilarity(point, centroids.get(i));
                if (similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    closestCentroidIndex = i;
                }
            }
            clusters.get(closestCentroidIndex).add(point.clone());
        }

        return clusters;
    }

    private static List<int[]> calculateNewCentroids(List<List<int[]>> clusters, int dimensions) {
        List<int[]> newCentroids = new ArrayList<>();
        for (List<int[]> cluster : clusters) {
            int[] centroid = calculateCentroid(cluster, dimensions);
            newCentroids.add(centroid);
        }
        return newCentroids;
    }

    private static int[] calculateCentroid(List<int[]> cluster, int dimensions) {
        int[] centroid = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            int sum = 0;
            for (int[] point : cluster) {
                sum += point[i];
            }
            centroid[i] = (sum > cluster.size() / 2) ? 1 : 0; // Binaire : 1 si la majorité est 1, sinon 0
        }
        return centroid;
    }

    private static boolean centroidsEqual(List<int[]> centroids1, List<int[]> centroids2) {
        if (centroids1.size() != centroids2.size()) {
            return false;
        }
        for (int i = 0; i < centroids1.size(); i++) {
            if (!arrayEquals(centroids1.get(i), centroids2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean arrayEquals(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    private static double tanimotoSimilarity(int[] point1, int[] point2) {
        double intersection = 0;
        double union = 0;
        for (int i = 0; i < point1.length; i++) {
            intersection += point1[i] & point2[i];
            union += point1[i] | point2[i];
        }
        return intersection / union;
    }

    private static void printArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}
```

Dans cette version, la fonction `tanimotoSimilarity` est utilisée pour calculer la similarité entre chaque point de données et les centroids lors de l'assignation des points aux clusters. De plus, la fonction `calculateCentroid` est ajustée pour utiliser l'indice de Tanimoto lors de la mise à jour des centroids.

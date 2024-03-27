# KNN method using tanimoto index

Voici la méthode `rowKNNSearch` qui effectue une recherche des k plus proches voisins pour une ligne donnée dans un tableau de données, en utilisant l'indice de Tanimoto :

```java
import java.util.Arrays;
import java.util.Comparator;

public class Test {
    public static void main(String[] args) throws Exception {
        double[][] binaryData = {
            { 1.0, 0.0, 0.0, 0.0, 0.0 }, 
            { 1.0, 1.0, 1.0, 1.0, 0.0 }, 
            { 0.0, 1.0, 1.0, 1.0, 1.0 },
            { 1.0, 0.0, 1.0, 1.0, 1.0 }, 
            { 0.0, 0.0, 1.0, 1.0, 1.0 }, 
            { 1.0, 1.0, 1.0, 0.0, 0.0 },
            { 0.0, 0.0, 1.0, 0.0, 1.0 } };

        int k = 3; // Nombre de plus proches voisins à trouver
        double[] queryRow = binaryData[binaryData.length - 1]; // Dernière ligne du tableau binaryData

        // Recherche des k plus proches voisins
        rowKNNSearch(binaryData, queryRow, k);
    }

    public static void rowKNNSearch(double[][] data, double[] queryRow, int k) {
        // Calcul des similarités entre la ligne de requête et les autres lignes
        double[][] similarities = new double[data.length - 1][2];
        for (int i = 0; i < data.length - 1; i++) {
            similarities[i][0] = i; // Indice de la ligne
            similarities[i][1] = tanimoto(queryRow, data[i]); // Similarité
        }

        // Trie des résultats par ordre décroissant de similarité
        Arrays.sort(similarities, Comparator.comparingDouble(a -> -a[1]));

        // Affichage des k plus proches voisins
        System.out.println("Les " + k + " plus proches voisins de la ligne sont :");
        for (int i = 0; i < k; i++) {
            int index = (int) similarities[i][0];
            System.out.println("Ligne " + index + " - Similarité : " + similarities[i][1]);
        }
    }

    public static double tanimoto(double[] vector1, double[] vector2) {
        double intersection = 0;
        double union = 0;
        for (int i = 0; i < vector1.length; i++) {
            if (vector1[i] == 1.0 && vector2[i] == 1.0) {
                intersection++;
            }
            if (vector1[i] == 1.0 || vector2[i] == 1.0) {
                union++;
            }
        }
        return intersection / union;
    }
}
```

Cette méthode `rowKNNSearch` prend en paramètres le tableau de données `data`, la ligne de requête `queryRow` et le nombre `k` de plus proches voisins à trouver. Elle calcule les similarités entre la ligne de requête et toutes les autres lignes, trie les résultats par ordre décroissant de similarité, puis affiche les k premiers voisins.
[Tanimoto index on Github](https://github.com/rootoor-dev/pairwise-comparison/tree/main)

# Determine k (neighbours number) automatically

## les techniques connues

Dans la méthode des k plus proches voisins (KNN), le choix de la valeur de k est crucial car elle influence directement les performances du modèle. Voici quelques techniques couramment utilisées pour déterminer la meilleure valeur de k :

1. **Validation croisée (Cross-Validation) :** La validation croisée consiste à diviser le jeu de données en ensembles d'apprentissage et de validation plusieurs fois, en utilisant différentes subdivisions des données. Pour chaque subdivision, le modèle est entraîné avec différentes valeurs de k et évalué sur l'ensemble de validation. La valeur de k qui produit les meilleures performances moyennes sur l'ensemble de validation est sélectionnée.

2. **Validation croisée en k blocs (k-Fold Cross-Validation) :** C'est une variante de la validation croisée où les données sont divisées en k sous-ensembles (ou blocs) de taille égale. Le modèle est entraîné k fois, chacune des k sous-ensembles étant utilisée comme ensemble de validation une fois, tandis que les k-1 autres blocs sont utilisés comme ensemble d'apprentissage. Encore une fois, différentes valeurs de k sont testées et évaluées, et la valeur optimale est choisie.

3. **Validation croisée leave-one-out (LOO) :** Il s'agit d'un cas particulier de validation croisée où un seul échantillon est retenu comme ensemble de validation tandis que tous les autres sont utilisés comme ensemble d'apprentissage. Cela est répété pour chaque échantillon dans le jeu de données. Bien que cela puisse être coûteux en termes de calcul, surtout pour de grands ensembles de données, cela peut fournir une estimation robuste de la performance du modèle pour différentes valeurs de k.

4. **Critère du coude (Elbow Method) :** Cette méthode consiste à tracer la valeur de la performance du modèle (par exemple, l'erreur de validation) par rapport à différentes valeurs de k. On recherche alors le point où la performance cesse de s'améliorer significativement à mesure que k augmente. Ce point est souvent appelé le "coude", et la valeur de k à ce point peut être choisie comme la meilleure.

5. **Grid Search et Recherche aléatoire (Random Search) :** Ces techniques consistent à définir une grille de valeurs possibles pour k (dans un certain intervalle) et à évaluer le modèle pour chaque valeur de la grille. Grid Search teste toutes les combinaisons de valeurs spécifiées, tandis que la recherche aléatoire sélectionne aléatoirement un sous-ensemble des combinaisons possibles. Ces méthodes peuvent être coûteuses en termes de calcul mais peuvent être efficaces pour rechercher efficacement de grandes plages de valeurs.

6. **Distance-based Methods (Méthodes basées sur la distance) :** Ces méthodes utilisent des caractéristiques des données pour estimer une distance optimale entre les points. Par exemple, la méthode de la distance moyenne pondérée peut être utilisée pour estimer une distance optimale entre les points du jeu de données, ce qui peut influencer le choix de k.

7. **Critères d'information :** Des critères d'information tels que le critère d'information bayésien (BIC) ou le critère d'information d'Akaike (AIC) peuvent être utilisés pour comparer différents modèles KNN avec différentes valeurs de k. Le modèle avec la valeur de k qui minimise le critère d'information peut être sélectionné comme le meilleur modèle.

8. **Heuristiques basées sur la taille du jeu de données :** Parfois, des heuristiques simples basées sur la taille du jeu de données sont utilisées pour choisir k. Par exemple, certaines règles empiriques recommandent de choisir k comme la racine carrée du nombre total d'observations dans le jeu de données.

9. **Validation Out-of-Bag (OOB) :** Si vous utilisez un algorithme de KNN en combinaison avec le Bagging (par exemple, Bagging de KNN), vous pouvez utiliser la méthode de validation out-of-bag. Dans cette méthode, certains échantillons ne sont pas utilisés lors de l'entraînement du modèle (échantillons "out-of-bag"). Ces échantillons peuvent ensuite être utilisés pour évaluer le modèle pour différentes valeurs de k, et la valeur optimale de k peut être sélectionnée en fonction des performances sur ces échantillons.

10. **Méthode de l'estimation de la densité locale (Local Density Estimation) :** Cette méthode consiste à estimer la densité locale des points dans l'espace des caractéristiques pour différents valeurs de k. En utilisant des techniques telles que la méthode du noyau (kernel method), vous pouvez estimer la densité des points autour de chaque point de données pour différentes valeurs de k et choisir celle qui optimise la performance du modèle.

11. **Méthodes basées sur la dissimilarité des voisins (Neighbor Dissimilarity-Based Methods) :** Ces méthodes cherchent à identifier la dissimilarité entre les voisins les plus proches pour différentes valeurs de k. En utilisant des métriques de dissimilarité telles que la variance, la covariance ou d'autres mesures de dispersion entre les voisins, vous pouvez choisir la valeur de k qui minimise la dissimilarité tout en maximisant les performances du modèle.

12. **Techniques de pondération des voisins (Neighbor Weighting Techniques) :** Au lieu d'utiliser une approche brute où tous les voisins contribuent de manière égale à la prédiction, certaines techniques pondèrent les contributions des voisins en fonction de leur proximité avec le point à prédire. En testant différentes fonctions de pondération pour différents valeurs de k, vous pouvez choisir la combinaison qui améliore les performances du modèle.

13. **Optimisation bayésienne :** L'optimisation bayésienne est une méthode d'optimisation itérative qui peut être utilisée pour trouver la valeur optimale de k en explorant efficacement l'espace des hyperparamètres. En utilisant des techniques telles que l'optimisation par acquisition (acquisition function) et les modèles de régression bayésienne, vous pouvez itérer pour trouver la meilleure valeur de k en minimisant ou maximisant une fonction objectif définie.

14. **Techniques d'apprentissage automatique (Machine Learning Techniques) :** Des techniques d'apprentissage automatique telles que l'apprentissage supervisé ou l'apprentissage semi-supervisé peuvent être utilisées pour prédire la valeur optimale de k en se basant sur des modèles pré-entraînés ou en utilisant des ensembles de données similaires pour l'apprentissage.



## code Java de test d'une des techniques

Déterminer automatiquement la valeur de k dans l'algorithme des k plus proches voisins peut se faire de différentes manières. Deux méthodes couramment utilisées sont :

1. **Validation croisée (Cross-validation) :** Vous pouvez diviser vos données en ensembles d'apprentissage et de test, puis exécuter l'algorithme des k plus proches voisins pour différentes valeurs de k. En utilisant la validation croisée, vous pouvez évaluer les performances de l'algorithme pour chaque valeur de k et sélectionner celle qui donne les meilleurs résultats.

2. **Validation basée sur les performances :** Vous pouvez mesurer les performances de votre algorithme en termes de précision, de rappel, de F-score ou d'autres métriques pertinentes pour votre problème. Ensuite, vous pouvez choisir la valeur de k qui maximise ou optimise ces performances.

Voici comment vous pourriez implémenter la première méthode, la validation croisée, pour déterminer automatiquement la valeur de k :

```java
public class Test {
    public static void main(String[] args) throws Exception {
        double[][] binaryData = {
            { 1.0, 0.0, 0.0, 0.0, 0.0 }, 
            { 1.0, 1.0, 1.0, 1.0, 0.0 }, 
            { 0.0, 1.0, 1.0, 1.0, 1.0 },
            { 1.0, 0.0, 1.0, 1.0, 1.0 }, 
            { 0.0, 0.0, 1.0, 1.0, 1.0 }, 
            { 1.0, 1.0, 1.0, 0.0, 0.0 },
            { 0.0, 0.0, 1.0, 0.0, 1.0 } };

        // Utilisation de la validation croisée pour déterminer la meilleure valeur de k
        int bestK = determineBestK(binaryData);
        System.out.println("La meilleure valeur de k est : " + bestK);
        
        // Maintenant, vous pouvez utiliser la méthode rowKNNSearch avec bestK
    }

    public static int determineBestK(double[][] data) {
        // Diviser les données en ensembles d'apprentissage et de test (par exemple, 80% pour l'apprentissage et 20% pour le test)
        int trainSize = (int) (data.length * 0.8);
        double[][] trainData = Arrays.copyOfRange(data, 0, trainSize);
        double[][] testData = Arrays.copyOfRange(data, trainSize, data.length);

        // Valeurs de k à tester
        int maxK = Math.min(trainSize, 10); // Par exemple, on teste jusqu'à k=10
        int bestK = 1;
        double bestAccuracy = 0.0;

        // Validation croisée pour chaque valeur de k
        for (int k = 1; k <= maxK; k++) {
            double accuracy = crossValidation(trainData, testData, k);
            if (accuracy > bestAccuracy) {
                bestAccuracy = accuracy;
                bestK = k;
            }
        }

        return bestK;
    }

    public static double crossValidation(double[][] trainData, double[][] testData, int k) {
        // Pour chaque ligne de testData, exécuter rowKNNSearch avec k et mesurer la précision
        int correctPredictions = 0;
        for (double[] queryRow : testData) {
            int predictedLabel = rowKNNSearch(trainData, queryRow, k);
            // Comparer la prédiction avec la vraie classe (dans le cas supervisé)
            // Et incrémenter correctPredictions si la prédiction est correcte
            // Vous devrez adapter cette partie en fonction de votre problème spécifique
        }

        // Calculer la précision moyenne sur l'ensemble de test
        return (double) correctPredictions / testData.length;
    }

    public static int rowKNNSearch(double[][] data, double[] queryRow, int k) {
        // Implémentation de rowKNNSearch comme précédemment, mais au lieu d'imprimer les résultats, retournez la prédiction
        // Vous devrez adapter cette partie en fonction de votre problème spécifique
        return 0; // Placeholder, veuillez implémenter votre logique de prédiction ici
    }

    public static double tanimoto(double[] vector1, double[] vector2) {
        // Implémentation de Tanimoto comme précédemment
        return 0.0; // Placeholder, veuillez implémenter votre calcul de similarité ici
    }
}
```

Cette implémentation de la validation croisée vous permettra de trouver la meilleure valeur de k pour votre ensemble de données en testant différentes valeurs de k et en mesurant les performances de l'algorithme des k plus proches voisins. Vous pouvez ensuite utiliser la valeur de k optimale pour effectuer la recherche des plus proches voisins sur votre ensemble de données complet.

package com.example.k_mean;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class KMeanApplication {
    public static int k; // number of clusters
    public static int maxIterations=10; // maximum number of iterations
    public static double  thresholdDistance=6;
    public static List<List<Double>> centroids=new ArrayList<>(k); // list of centroid vectors
    public static List<List<List<Double>>> clusters=new ArrayList<>(k); // list of data points assigned to each cluster
    public static Random random = new Random();
    public static List<List<List<Double>>> cluster(List<List<Double>> data) {
        // Initialize centroids randomly
        for (int i = 0; i < k; i++) {
            int randomIndex = random.nextInt(data.size());
            centroids.add(data.get(randomIndex));
            clusters.add(new ArrayList<>());
        }
        // Assign data points to clusters
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            //clear clusters
            for (List<List<Double>> cluster : clusters) {
                cluster.clear();
            }
            //getClosestCentroidIndex
            for (List<Double> point : data) {
                int closestCentroidIndex = getClosestCentroidIndex(point);
                clusters.get(closestCentroidIndex).add(point);
            }
            // Recalculate centroids
            boolean hasConverged = true;
            for (int i = 0; i < k; i++) {
                List<Double> oldCentroid = centroids.get(i);
                //List<Double> newCentroid =new ArrayList<Double>();
                List<Double> newCentroid = calculateCentroid(clusters.get(i));
                centroids.set(i, newCentroid);
                if (distance(oldCentroid, newCentroid) > 0.00001) {
                    hasConverged = false;
                }
            }
            if (hasConverged) {
                break;
            }
        }
        return clusters;
    }
    public static int getClosestCentroidIndex(List<Double> point) {
        int closestCentroidIndex = 0;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < centroids.size(); i++) {
            List<Double> centroid = centroids.get(i);
            double distance = distance(point, centroid);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCentroidIndex = i;
            }
        }
        return closestCentroidIndex;
    }
    public static List<Double> calculateCentroid(List<List<Double>> cluster) {
        int dimensions = cluster.get(0).size();//dimensions for one point
        Double[] centroid = new Double[dimensions];//initialize new centroid List
        int clusterSize = cluster.size();
        //calculate Sum for each cell
        for (List<Double> point : cluster) {
            for (int i = 0; i < dimensions; i++) {
                if(centroid[i]==null) centroid[i]=0.0;
                centroid[i] += point.get(i);
            }
        }
        // divide each cell by clusterSize
        for (int i = 0; i < dimensions; i++) {
            centroid[i] /= clusterSize;
        }
        return Arrays.asList(centroid);
    }
    public static List<String> ReturnUSer(List<List<Double>> nodes,List<List<Double>> cluster){
        List<String> Result=new ArrayList<>();
        for (List<Double> point:cluster
             ) {
            Result.add("User: "+nodes.indexOf(point));
        }
        return Result;
    }
    public static double distance(List<Double> a, List<Double> b) {
        double sum = 0.0;
        for (int i = 0; i < a.size(); i++) {
            double diff = a.get(i) - b.get(i);
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
    public static List<List<List<Double>>> deleteOutliers(List<List<Double>> nodes,List<List<List<Double>>> clusters,List<List<Double>> centroids,double  thresholdDistance){
        System.out.println("****************************************Outliers**************************************");
        for (int i = 0; i < centroids.size(); i++) {
            List<String> Result=new ArrayList<>();
            List<Double> centroid = centroids.get(i);
            List<List<Double>> cluster=clusters.get(i);
            for (List<Double> point :cluster) {
                double distance = distance(point, centroid);
                if (distance>thresholdDistance){
                    Result.add("User: "+nodes.indexOf(point));
                    int index=cluster.indexOf(point);
                    clusters.remove(clusters.get(i).get(index));
                }
            }
            System.out.println(Result);
            System.out.println("-----------------");
        }
        return clusters;
    }
    public static void main(String[] args) {
    ExcelReader reader = new ExcelReader();
    List<List<Double>> nodes = reader.readExcelFile("Review_ratings.xlsx");
    Scanner input = new Scanner(System.in);
    System.out.println("Enter K");
    k = input.nextInt();
    System.out.println("Enter maxIterations");
    maxIterations = input.nextInt();
    System.out.println("Enter thresholdDistance");
    thresholdDistance = input.nextInt();
    List<List<List<Double>>> clusters= cluster(nodes);
    List<List<List<Double>>> newClusters=deleteOutliers(nodes,clusters,centroids,thresholdDistance);
        System.out.println("****************************************Clusters**************************************");
    for (List<List<Double>> cluster:newClusters){
            System.out.println(cluster);
            System.out.println("------------------------------------------------------");
            System.out.println(ReturnUSer(nodes,cluster));
            System.out.println();
            System.out.println();
        }
}
}





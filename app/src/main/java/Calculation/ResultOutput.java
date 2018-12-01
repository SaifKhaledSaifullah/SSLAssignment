package Calculation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class ResultOutput {
    private int duration=0;
    private ArrayList<int[]> resultList = new ArrayList<int[]>();

    public ResultOutput()
    {

    }


    private  void combinationUtil(ArrayList<Integer> list, ArrayList<Integer> data, int start, int end, int index, int r) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            int sum=0;
            int[] result = new int[r];
            for (int j = 0; j < r; j++)
            {
                sum=sum+data.get(j);
                result[j]=data.get(j);
            }
            if(sum<=duration)
            {
                resultList.add(result);
            }

            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data.set(index, list.get(i));
            combinationUtil(list, data, i + 1, end, index + 1, r);
        }
    }

    // The main function that prints all combinations of size r
    // in list of size n. This function mainly uses combinationUtil()
    public ArrayList<int[]> printCombination(ArrayList<Integer> list, int n, int r, int d) {
        duration=d;
        // A temporary array to store all combination one by one
        ArrayList<Integer> data = new ArrayList<>(Collections.nCopies(r, 0));

        // Print all combination using temporary array 'data'
        combinationUtil(list, data, 0, n - 1, 0, r);

       // Log.e("ResultOutput:","Size of resultLIst: "+resultList.size()+"\n");

        return resultList;
    }
}

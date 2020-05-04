package temp;

public class TestSelectionSort {

	public static void main(String[] args) {
		int[] a = { 2, 20, 3, 4, 1, 5 };
		System.out.println(a);

		for (int i = 0; i < a.length; i++) {

			// fin min ele and swap
			// int min = findMinElement(a,i+1);

			int j = i + 1;
			int min = a[j];
			int min_index = j;
			for (int k = j; k < a.length; k++) {

				if (a[k] < min) {
					min = a[k];
					min_index = k;
				}

			}

			int temp = a[i];
			a[i] = min;
			a[min_index] = temp;

		}

		System.out.println(a);

	}

}

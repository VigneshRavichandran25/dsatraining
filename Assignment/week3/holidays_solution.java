class sortedarrayduplicates{
    public int removeduplicates(int[] arr){
        if (arr.length==0){
            return 0;
        }
        int ptr=1;

        for(int i=1;i<arr.length;i++){
            if(arr[i]!=arr[i-1]){
                arr[ptr]=arr[i];
                ptr++;
            }
        }
        return ptr;
    }
    public void uniqueprintelements(int[] arr , int size){
        for(int i=0;i<size;i++){
            System.out.print(arr[i]+ " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        sortedarrayduplicates rem = new sortedarrayduplicates();
        int[] arr={1,1,2,2,3,4,5};
        
        System.out.println("original array values :");
        for(int num:arr){
            System.out.print(num+" ");
        }
        System.out.println();

        int newSize = rem.removeduplicates(arr);
        System.out.println("unique array values : ");
        rem.uniqueprintelements(arr,newSize);
        
        System.out.println("new logical size : "+ newSize);    
    }
}

class reverseaarraydisplay{
    public void printoriginalarray(int[] arr){
         System.out.println("original array values are: ");

        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+ " ");
        }
        System.out.println();
    }

     public void printreversearray(int[] arr){
         System.out.println("Reverse array values are: ");

        for(int i=arr.length-1;i>=0;i--){
            System.out.print(arr[i]+ " ");
        }
        System.out.println();
    }

     public static void main(String[] args){

        int[] array={12,13,14,15,16,90,89};
        reverseaarraydisplay reverse = new  reverseaarraydisplay();
        reverse.printoriginalarray(array);
        reverse.printreversearray(array);

    }

}
class OddEvenCounter {

    public int evenCounter(int[] arr) {
        int even = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                even++;
            }
        }
        return even;
    }

    public int oddCounter(int[] arr) {
        int odd = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 != 0) {
                odd++;
            }
        }
        return odd;
    }
    
    public void printevenvalues(int[] arr) {
        System.out.print("Even Elements: ");

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                System.out.print(arr[i] + " ");
            }
        }
        System.out.println();
    }
     public void printoddvalues(int[] arr) {
        System.out.print("Even Elements: ");

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 != 0) {
                System.out.print(arr[i] + " ");
            }
        }
        System.out.println();
     }

    public void printArray(int[] arr) {
        System.out.println("Array Elements:");

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] numbers = {21, 34, 57, 88, 90, 45, 33, 24, 12};

        OddEvenCounter counter = new OddEvenCounter();

        counter.printArray(numbers);
        counter.printoddvalues(numbers);
        counter.printevenvalues(numbers);

        System.out.println("Odd values : " + counter.oddCounter(numbers));
        System.out.println("Even values : " + counter.evenCounter(numbers));
    }
}
class minarray {

    public int findmin(int[] arr) {
        int min = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public int findmax(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public void printarrays(int[] arr) {
        System.out.println("arrays elements");

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        int[] numbers = {45, 34, 56, 89, 3, 44, 23, 45};

        minarray obj = new minarray();

        obj.printarrays(numbers);
        System.out.println("min values : " + obj.findmin(numbers));
        System.out.println("max values : " + obj.findmax(numbers));
    }
}
class linearsearch{
    public int searchelement(int[] arr,int target){
        for(int i=0;i<arr.length;i++){
            if(arr[i] == target){
                return i;
            }
        }
        return -1;
    }
    public void printarrays(int[] arr){
        System.out.println("array values are : ");

        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i]+ " ");
        }
        System.out.println();

    }
    public static void main(String[] args){

        int[] arr={12,56,78,90,45,33,3,45};
        linearsearch search = new linearsearch();
        search.printarrays(arr);
         
        int target1 = 90;
        int result1 = search.searchelement(arr,target1);
        if(result1 !=-1){
            System.out.println(target1+" found at index "+result1);
        }else{
            System.out.println(target1+"not found");
        }
        int target2 = 100;
        int result2 = search.searchelement(arr,target2);
        if(result2 !=-1){
            System.out.println(target2+" found at index "+result2);
        }else{
            System.out.println(target2+" not found");
        }
    }
}

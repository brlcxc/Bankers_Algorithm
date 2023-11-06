public class BankerAlgorithm {
    //total number of processes
    int processNum;
    //total number of resource types
    int resourceTypeNum;
    //total number of each resource available
    int[] resourceAvailability;
    //The maximum of a resource that a particular process might need
    int[][] maxAllocation;
    //how many of a respective resource is already located to a particular process
    int[][] currentProcessAllocation;
    //The amount of a respective resource needed to complete the task
    int[][] processNeed;
    SafetyAlgorithm safetyAlgorithm;
    BankerAlgorithm(int[] resourceAvailability, int[][] currentProcessAllocation, int[][] maxAllocation){
        this.resourceAvailability = resourceAvailability;
        this.currentProcessAllocation = currentProcessAllocation;
        this.maxAllocation = maxAllocation;

        resourceTypeNum = resourceAvailability.length;
        processNum = currentProcessAllocation.length;

        processNeed = new int[processNum][resourceTypeNum];

        //the need for each process is calculated
        for(int process = 0; process < processNum; process++){
            for(int resource = 0; resource < resourceTypeNum; resource++){
                processNeed[process][resource] = maxAllocation[process][resource] - currentProcessAllocation[process][resource];
            }
        }
        safetyAlgorithm = new SafetyAlgorithm();
        safetyAlgorithm.scheduleProcesses();
    }
    class SafetyAlgorithm{
        Boolean[] processCompletionStatus;
        int completionCount;

        SafetyAlgorithm(){
            processCompletionStatus = new Boolean[processNum];
            completionCount = 0;

            for(int i = 0; i < processNum; i++){
                processCompletionStatus[i] = false;
            }
        }
        public void scheduleProcesses(){
            //The loop will run until all processes have been scheduled
            while(completionCount < processNum) {
                for (int i = 0; i < processNum; i++) {
                    //this flag is set to true if there are enough resources available for an unscheduled process
                    Boolean flag = true;
                    if (!processCompletionStatus[i]) {
                        for (int j = 0; j < resourceTypeNum; j++) {
                            if (!(processNeed[i][j] <= resourceAvailability[j])) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            System.out.println("Process " + i + " scheduled");
                            for (int j = 0; j < resourceTypeNum; j++) {
                                int processRequest = processNeed[i][j];

                                //each type of resources is being removed from the system and given to the process as needed
                                resourceAvailability[j] -= processRequest;
                                currentProcessAllocation[i][j] += processRequest;
                                processNeed[i][j] = 0;

                                //the resources return back to the system and the status of the process is changed to completed
                                resourceAvailability[j] += currentProcessAllocation[i][j];
                                currentProcessAllocation[i][j] = 0;
                                processCompletionStatus[i] = true;
                            }
                            completionCount++;
                        }
                    }
                }
            }
            //The system is safe when all processes have been scheduled
            System.out.println("System is in safe state");
        }
    }
    public static void main(String[] args) {
        int[] resourceAvailability = new int[]{3,3,2};

        int[][] currentProcessAllocation = new int[][]{
                {0, 1, 0},
                {2, 0, 0},
                {3, 0, 2},
                {2, 1, 1},
                {0, 0, 2}};
        int[][] maxAllocation = new int[][]{
                {7, 5, 3},
                {3, 2, 2},
                {9, 0, 2},
                {2, 2, 2},
                {4, 3, 3}};

        BankerAlgorithm bankerAlgorithm = new BankerAlgorithm(resourceAvailability, currentProcessAllocation, maxAllocation);
    }
}


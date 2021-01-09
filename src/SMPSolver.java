import java.util.ArrayList;

public class SMPSolver {

    private ArrayList <Student> S;
    private ArrayList <School> R;
    private double avgSuitorRegret ;
    private double avgReceiverRegret ;
    private double avgTotalRegret ;
    private boolean matchesExist;


    //constructors
    public SMPSolver(){
        this.matchesExist = false;
    }

    public SMPSolver(ArrayList<Student> S, ArrayList <School> R){
        this.S = new ArrayList<Student>(S);
        this.R = new ArrayList<School>(R);
        avgSuitorRegret = -1;
        avgReceiverRegret = -1;
        avgTotalRegret = -1;
    }


    //getters
    public double getAvgSuitorRegret(){
        return this.avgSuitorRegret;
    }

    public double getAvgReceiverRegret(){
        return this.avgReceiverRegret;
    }

    public double getAvgTotalRegret(){
        return this.avgTotalRegret;
    }

    public boolean matchesExist(){
        return this.matchesExist;
    }


    //reset everything with new suitors and receivers
    public void reset(ArrayList<Student> S, ArrayList<School> R){
        this.S = new ArrayList<Student>(S);
        this.R = new ArrayList<School>(R);
        this.avgSuitorRegret = -1;
        this.avgReceiverRegret = -1;
        this.avgTotalRegret = -1;
        this.matchesExist = false;
    }


    //methods for matching
    public boolean match() {
        if (!matchingCanProceed()) {
            return false;
        }
        else {
            long start = System.currentTimeMillis();
            int size = S.size();
            ArrayList<Integer> availableStudents = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                availableStudents.add(i);
            }
            for (int k = 0; k < availableStudents.size(); k++) {

                int currStudent = availableStudents.get(k);

                for (int i = 0; i < size; i++) {
                    int currSchool = S.get(currStudent).getRanking(i) - 1;
                    int matchedStudent = R.get(currSchool).getStudent();
                    if (matchedStudent == -1) {
                        makeEngagement(currStudent, currSchool);
                        break;
                    }
                    else if(makeProposal(currStudent, currSchool)){
                        makeEngagement(currStudent, currSchool);
                        availableStudents.add(matchedStudent);
                        break;
                    }
                }
            }
            calcRegrets();
            printStats();
            this.matchesExist = true;
            long elapsedTime = System.currentTimeMillis() - start;
            System.out.println("\n" + size + " matches made in " + elapsedTime + "ms!");
            return true;
        }
    }

    private boolean makeProposal(int suitor, int receiver){
        return (R.get(receiver).findRankingByID(suitor) < R.get(receiver).findRankingByID(R.get(receiver).getStudent()));
    }

    private void makeEngagement(int suitor, int receiver){
        this.R.get(receiver).setStudent(suitor);
        this.S.get(suitor).setSchool(receiver);
        this.R.get(receiver).setRegret(this.R.get(receiver).findRankingByID(suitor));
        this.S.get(suitor).setRegret(this.S.get(suitor).findRankingByID(receiver));
    }

    public boolean matchingCanProceed(){
       if(this.S.size() == 0){
            System.out.println("\nERROR: No suitors are loaded!");
            return false;
        }
        else if(this.R.size() == 0){
            System.out.println("\nERROR: No receivers are loaded!");
            return false;
        }
        else if(this.S.size() != this.R.size()){
            System.out.println("\nERROR: The number of suitors and receivers must be equal!");
            return false;
        }
        return true;
    }

    public void calcRegrets(){
        double totalStudentRegret = 0.0;
        double totalSchoolRegret = 0.0;
        for(int a = 0; a < this.S.size(); a++){
            totalStudentRegret += S.get(a).getRegret();
            totalSchoolRegret += R.get(a).getRegret();
        }
        this.avgSuitorRegret = totalStudentRegret / this.S.size();
        this.avgReceiverRegret = totalSchoolRegret / this.R.size();
        this.avgTotalRegret = (totalSchoolRegret + totalStudentRegret)/(this.S.size()*2);
    }

    public boolean isStable(){
        for(int i = 0; i < this.R.size(); i++){
            int j = 0;
            while(j < this.S.get(i).getRegret()){
                int rank1 = this.R.get(this.S.get(i).getRanking(j)-1).findRankingByID(this.R.get(this.S.get(i).getRanking(j)-1).getStudent());
                int rank2 = this.R.get(this.S.get(i).getRanking(j)-1).findRankingByID(i);
                if(rank1 > rank2){
                    return false;
                }
                j++;
            }
        }
        return true;
    }


    //print methods
    public void print(){
        if(this.matchesExist){
            printMatches();
            printStats();
        }
        else{
            System.out.println("\nERROR: No matches exist!");
        }
    }

    public void printMatches() {
        System.out.println("\nMatches:\n--------");
        for (int i = 0; i < S.size(); i++) {
            System.out.format("%s: %s\n", this.R.get(i).getName(), this.S.get(this.R.get(i).getStudent()).getName());
        }
    }

    public void printStats(){
        String s = (isStable()? "Yes" : "No");
        System.out.format("\nStable matching? %s\n", s);
        System.out.format("Average student regret: %.2f\n", this.avgSuitorRegret);
        System.out.format("Average school regret: %.2f\n", this.avgReceiverRegret);
        System.out.format("Average total regret: %.2f\n", this.avgTotalRegret);
    }

}

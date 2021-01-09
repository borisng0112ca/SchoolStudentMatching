import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class School {
    private String name;
    private double alpha;
    private int[] rankings;
    private int student;
    private int regret;


    //constructors
    public School() {
        this.student = -1;
        this.regret = -1;
    }

    public School(String name, double alpha, int nStudents) {
        this.name = name;
        this.alpha = alpha;
        this.rankings = new int[nStudents];
        this.student = -1;
        this.regret = -1;
    }


    //getters
    public String getName(){
        return this.name;
    }

    public double getAlpha(){
        return this.alpha;
    }

    public int getRanking(int i){
        return this.rankings[i];
    }

    public int getStudent(){
        return this.student;
    }

    public int getRegret(){
        return this.regret;
    }


    //setters
    public void setName(String name){
        this.name = name;
    }

    public void setAlpha(double alpha){
        this.alpha = alpha;
    }

    public void setRanking(int i, int r){
        this.rankings[i] = r;
    }

    public void setStudent(int i){
        this.student = i;
    }

    public void setRegret(int r){
        this.regret = r;
    }

    public void setNStudents(int n){
        this.rankings = new int[n];
    }


    //utility functions
    public void calcRankings(ArrayList <Student> S){
        int len = this.rankings.length;
        boolean tied;
        double composite[] = new double[len];
        double compositeSorted[] = new double[len];
        for(int i = 0; i < len; i++){
            composite[i] = this.alpha * S.get(i).getGPA() + (1 - this.alpha) * S.get(i).getES();
            compositeSorted[i] = composite[i];
        }
        Arrays.sort(compositeSorted);

        for(int j = 0; j < len; j++){
            for (int k = 0; k < len; k++){
                if(composite[k] == compositeSorted[len - j - 1]){
                    tied = false;
                    for(int l = 0; l < j; l++){
                        if(this.rankings[l] == k){
                            tied = true;
                            break;
                        }
                    }
                    if(!tied){
                        this.rankings[j] = k;
                        break;
                    }
                }
            }
        }
    }

    public int findRankingByID(int ind){
        for(int i = 0; i < this.rankings.length; i++){
            if(this.rankings[i] == ind){
                return i;
            }
        }
        return -1;
    }

    public void editInfo(ArrayList <Student> S, boolean canEditRankings) throws IOException{
        System.out.print("\nName: ");
        setName(Pro4_ngboris1.cin.readLine());
        setAlpha(Pro4_ngboris1.getDouble("GPA weight: ", 0.0, 1.0));
        if(canEditRankings){
            this.calcRankings(S);
        }
    }


    //print methods
    public void print(ArrayList <Student> S, boolean rankingsSet){
        System.out.format("%-27s  %7.2f  %-27s", this.name, this.alpha, (this.student == -1)? "-" : S.get(this.student).getName());
        if(rankingsSet){
            printRankings(S);
        }
        else{
            System.out.print("-");
        }
    }

    public void printRankings(ArrayList <Student> S){
        System.out.format("%s", S.get(this.rankings[0]).getName());
        for (int i = 1; i < this.rankings.length; i++) {
            System.out.format(", %s", S.get(this.rankings[i]).getName());
        }
    }

    public boolean isValid() {
        return ((this.alpha < 0 || this.alpha > 1) ? false : true);
    }
}
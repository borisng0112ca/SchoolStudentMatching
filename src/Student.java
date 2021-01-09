import java.util.ArrayList;
import java.io.IOException;

public class Student {
    private String name;
    private double GPA;
    private int ES;
    private int[] rankings;
    private int school;
    private int regret;


    //constructors
    public Student(){
        this.school = -1;
        this.regret = -1;
    }

    public Student(String name, double GPA, int ES, int nSchools){
        this.name = name;
        this.GPA = GPA;
        this.ES = ES;
        this.rankings = new int [nSchools];
        this.school = -1;
        this.regret = -1;
    }


    //getters
    public String getName(){
        return this.name;
    }

    public double getGPA() {
        return this.GPA;
    }

    public int getES() {
        return this.ES;
    }

    public int getRanking(int i){
        return this.rankings[i];
    }

    public int getSchool(){
        return this.school;
    }

    public int getRegret(){
        return this.regret;
    }


    //setters
    public void setName(String name){
        this.name = name;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public void setES(int ES){
        this.ES = ES;
    }

    public void setRanking(int i, int r){
        this.rankings[i] = r;
    }

    public void setSchool(int i){
        this.school = i;
    }

    public void setRegret(int r){
        this.regret = r;
    }

    public void setNSchools(int n){
        this.rankings = new int [n];
    }


    //utility functions
    public int findRankingByID(int ind){
        for(int i = 0; i <this.rankings.length; i++){
            if(this.rankings[i] == ind+1){
                return i;
            }
        }
        return -1;
    }

    public void editInfo(ArrayList<School> H, boolean canEditRankings) throws IOException {
        System.out.print("Name: ");
        this.setName(Pro4_ngboris1.cin.readLine());
        this.setGPA(Pro4_ngboris1.getDouble("GPA: ", 0.00, 4.00));
        this.setES(Pro4_ngboris1.getInteger("Extracurricular score: ", 0, 5));
        if(canEditRankings){
            boolean run = true;
            while(run){
                System.out.print("Edit rankings (y/n): ");
                String choice = Pro4_ngboris1.cin.readLine();
                if(choice.equals("y") || choice.equals("Y")){
                    this.editRankings(H, true);
                    run = false;
                }
                else if(choice.equals("n") || choice.equals("N")){
                    run = false;
                }
                else{
                    System.out.println("ERROR: Choice must be 'y' or 'n'!");
                    run = true;
                }
            }
        }
    }

    public void editRankings(ArrayList<School> H, boolean rankingsSet){
        int len = this.rankings.length;
        for(int i = 0; i < len; i++){
            this.rankings[i] = -2;
        }
        System.out.println("\nStudent " + this.name +  "'s rankings:");
        for(int j = 0; j < len; j++){
            boolean isEntered;
            int curr;
            do{
                curr = Pro4_ngboris1.getInteger("School " + H.get(j).getName() + ": ", 1, len);
                curr--;
                isEntered = false;
                if(this.rankings[curr] != -2){
                    System.out.println("ERROR: Rank " + (curr + 1) + " already used!\n");
                    isEntered = true;
                }
            }while(isEntered);
            this.rankings[curr] = j+1;
        }
        System.out.println("");
    }


    //print methods
    public void print(ArrayList<School> H, boolean rankingsSet){
        System.out.format("%-27s%8.2f%4d  %-27s", this.name, this.GPA, this.ES, (this.school == -1) ? "-" : H.get(this.school).getName());
        if(rankingsSet){
            printRankings(H);
        }
        else{
            System.out.print("-");
        }
    }

    public void printRankings(ArrayList<School> H){
        System.out.format("%s", H.get(this.rankings[0]-1).getName());
        for (int i = 1; i < this.rankings.length; i++) {
            System.out.format(", %s", H.get(this.rankings[i]-1).getName());
        }
    }

    public boolean isValid(){
        if(this.ES < 0 || this.ES > 5){
            return false;
        }
        if(this.GPA < 0 || this.GPA > 4){
            return false;
        }
        for (int i = 0; i < this.rankings.length; i++) {
            if(this.rankings[i] < 1 || this.rankings[i] > this.rankings.length){
                return false;
            }
            for (int j = i+1; j < this.rankings.length; j++) {
                if (this.rankings[i] == this.rankings[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

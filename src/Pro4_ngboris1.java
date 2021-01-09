import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Pro4_ngboris1 {

    //global bufferedReader created and initialized
    public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

    //function that displays menu to user
    public static void displayMenu() {
        System.out.println("JAVA STABLE MARRIAGE PROBLEM v2\n");
        System.out.println("L - Load students and schools from file");
        System.out.println("E - Edit students and schools");
        System.out.println("P - Print students and schools");
        System.out.println("M - Match students and schools using Gale-Shapley algorithm");
        System.out.println("D - Display matches and statistics");
        System.out.println("R - Reset database");
        System.out.println("Q - Quit");
        System.out.print("\nEnter choice: ");
    }

    //function that loads students info from a user-provided text file and return number of students
    public static int loadStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException{
        while(true){
            System.out.print("\nEnter student file name (0 to cancel): ");
            String fileName = cin.readLine();
            if(fileName.equals("0")){
                System.out.println("\nFile loading process canceled.");
                return 0;
            }
            try {
                BufferedReader fin = new BufferedReader(new FileReader(fileName));
                String currLine;
                int totalStudents = 0;
                int validStudents = 0;
                while((currLine = fin.readLine()) != null){
                    totalStudents++;
                    String[] splitString = currLine.split(",");
                    if(splitString.length-3 != H.size() || H.size() == 0){
                        continue;
                    }
                    Student newStudent = new Student(splitString[0], Double.parseDouble(splitString[1]),
                            Integer.parseInt(splitString[2]), H.size());
                    for(int i = 0; i < H.size(); i++){
                        newStudent.setRanking(i, Integer.parseInt(splitString[i+3]));
                    }
                    if(!newStudent.isValid()){
                        continue;
                    }
                    S.add(newStudent);
                    validStudents++;
                }
                fin.close();
                System.out.println("\n" + validStudents + " of " + totalStudents + " students loaded!");
                if(validStudents == 0){
                    S.clear();
                }
                break;
            }
            catch (IOException e) {
                System.out.println("\nERROR: File not found!");
            }
        }
        return S.size();
    }

    //function that loads schools info from a user-provided text file and return number of schools
    public static int loadSchools(ArrayList<School> H) throws IOException {
        while(true){
            System.out.print("\nEnter school file name (0 to cancel): ");
            String fileName = cin.readLine();
            if(fileName.equals("0")){
                System.out.println("\nFile loading process canceled.");
                return 0;
            }
            try {
                BufferedReader fin = new BufferedReader(new FileReader(fileName));
                String currLine;
                int totalSchools = 0;
                int validSchools = 0;
                while((currLine = fin.readLine()) != null){
                    String[] splitString = currLine.split(",");
                    School newSchool = new School(splitString[0], Double.parseDouble(splitString[1]), 0);
                    totalSchools++;
                    if(!newSchool.isValid()){
                        continue;
                    }
                    H.add(newSchool);
                    validSchools++;
                }
                fin.close();
                for(int i = 0; i < H.size(); i++){
                    H.get(i).setNStudents(H.size());
                }
                System.out.println("\n" + validSchools + " of " + totalSchools + " schools loaded!");
                break;
            }
            catch (IOException e) {
                System.out.println("\nERROR: File not found!");
            }
        }
        return H.size();
    }

    //function that edits the info of schools and students
    public static void editData(ArrayList<Student> S, ArrayList<School> H) throws IOException{
        while(true){
            System.out.print("\nEdit data\n---------\nS - Edit students\nH - Edit high schools\nQ - Quit\n\nEnter choice: ");
            String choice = cin.readLine();
            if(choice.equals("S") || choice.equals("s")){
                if(S.size() == 0){
                    System.out.println("\nERROR: No students are loaded!");
                }
                else{
                    editStudents(S, H);
                }
            }
            else if(choice.equals("H") || choice.equals("h")){
                if(H.size() == 0){
                    System.out.println("\nERROR: No schools are loaded!");
                }
                else{
                    editSchools(S, H);
                }
            }
            else if(choice.equals("Q") || choice.equals("q")){
                break;
            }
            else{
                System.out.print("\nERROR: Invalid menu choice!\n");
            }
        }
    }

    //function that allows user to edit students info
    public static void editStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException{
        while(true){
            printStudents(S, H);
            int index = getInteger("Enter student (0 to quit): ", 0, S.size());
            if(index == 0){
                break;
            }
            else{
                System.out.println();
                S.get(index-1).editInfo(H, true);
            }
            for(int j = 0; j < H.size(); j++){
                H.get(j).calcRankings(S);
            }
        }
    }

    //function that allows user to edit schools info
    public static void editSchools(ArrayList<Student> S, ArrayList<School> H) throws IOException {
        while(true) {
            printSchools(S, H);
            int index = getInteger("Enter school (0 to quit): ", 0, S.size());
            if(index == 0){
                break;
            }
            else if(S.size() == H.size()){
                H.get(index-1).editInfo(S, true);
            }
            else{
                H.get(index-1).editInfo(S, false);
            }
        }
    }

    //function that prints students to user
    public static void printStudents(ArrayList<Student> S, ArrayList<School> H){
        System.out.format("\n%-3s %-27s%8s%4s  %-27s%-22s\n", " #", "Name", "GPA", "ES", "Assigned school", "Preferred school order");
        for(int i = 0; i < 94; i++){
            System.out.print("-");
        }
        System.out.println();
        for (int j = 0; j < S.size(); j++) {
            System.out.format("%2d. ", j + 1);
            if(S.size() == H.size()){
                S.get(j).print(H, true);
            }
            else{
                S.get(j).print(H, false);
            }
            System.out.println();
        }
        for(int k = 0; k < 94; k++){
            System.out.print("-");
        }
        System.out.println();
    }

    //function that prints schools to user
    public static void printSchools(ArrayList<Student> S, ArrayList<School> H){
        System.out.format("\n%-3s %-27s  %7s  %-27s%-23s\n", " #", "Name", "Weight", "Assigned student", "Preferred student order");
        for(int i = 0; i < 92; i++){
            System.out.print("-");
        }
        System.out.println();
        for (int j = 0; j < H.size(); j++) {
            System.out.format("%2d. ", j + 1);
            if(H.size() == S.size()){
                H.get(j).print(S, true);
            }
            else{
                H.get(j).print(S, false);
            }
            System.out.println();
        }
        for(int k = 0; k < 92; k++){
            System.out.print("-");
        }
        System.out.println();
    }

    //function that gets an integer value from user ranging from stated LB to UB
    public static int getInteger(String prompt, int LB, int UB){
        int value = 0;
        boolean isValid;
        do{
            System.out.print(prompt);
            try{
                value = Integer.parseInt(cin.readLine());
                isValid = true;
            }
            catch(NumberFormatException | IOException e){
                isValid = false;
            }
            if((value < LB || value > UB) && isValid){
                isValid = false;
            }
            else if(isValid){
                break;
            }
            if(UB == Integer.MAX_VALUE){
                System.out.format("\nERROR: Input must be an integer in [%d, infinity]!\n\n", LB);
            }
            else{
                System.out.format("\nERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
            }
        }while(!isValid);
        return value;
    }

    //function that gets an double value from user ranging from stated LB to UB
    public static double getDouble(String prompt, double LB, double UB){
        double value = 0;
        boolean isValid;
        do{
            System.out.print(prompt);
            try{
                value = Double.parseDouble(cin.readLine());
                isValid = true;
            }
            catch(NumberFormatException | IOException e){
                isValid = false;
            }
            if((value < LB || value > UB) && isValid){
                isValid = false;
            }
            else if(isValid){
                break;
            }
            if(UB == Double.MAX_VALUE){
                System.out.format("\nERROR: Input must be a real number in [%.2f, infinity]!\n\n", LB);
            }
            else{
                System.out.format("\nERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
            }
        }while(!isValid);
        return value;
    }

    public static void main(String args[]) throws IOException {

        ArrayList <Student> Students = new ArrayList<Student>();
        ArrayList <School> Schools = new ArrayList<School>();
        int nStudents = 0;
        int nSchools = 0;
        boolean matched = false;
        SMPSolver newMatch = new SMPSolver();

        while(true){
            displayMenu();
            String choice = cin.readLine();
            if(choice.equalsIgnoreCase("L")){
                nSchools = loadSchools(Schools);
                nStudents = loadStudents(Students, Schools);
                if(nStudents == nSchools && nSchools != 0 && nStudents != 0){
                    for(int i = 0; i < nSchools; i++){
                        Schools.get(i).calcRankings(Students);
                    }
                }
            }
            else if(choice.equalsIgnoreCase("E")){
                editData(Students, Schools);
            }
            else if(choice.equalsIgnoreCase("P")){
                if(nStudents != 0){
                    System.out.println("\nSTUDENTS:");
                    printStudents(Students, Schools);
                }
                else{
                    System.out.println("\nERROR: No students are loaded!");
                }
                if(nSchools != 0){
                    System.out.println("\nSCHOOLS:");
                    printSchools(Students, Schools);
                }
                else{
                    System.out.println("\nERROR: No schools are loaded!");
                }
            }
            else if(choice.equalsIgnoreCase("M")){
                for(int i = 0; i < nStudents; i++){
                    Students.get(i).setSchool(-1);
                    Schools.get(i).setStudent(-1);
                }
                 newMatch = new SMPSolver(Students, Schools);
                 matched = newMatch.match();
            }
            else if(choice.equalsIgnoreCase("D")){
                if(nStudents == nSchools && nSchools != 0 && nStudents != 0) {
                    for (int i = 0; i < nSchools; i++) {
                        Schools.get(i).calcRankings(Students);
                        Schools.get(i).setRegret(Schools.get(i).findRankingByID(Schools.get(i).getStudent()));
                        Students.get(i).setRegret(Students.get(i).findRankingByID(Students.get(i).getSchool()));
                    }
                }
                newMatch.calcRegrets();
                newMatch.print();
            }
            else if(choice.equalsIgnoreCase("R")){
                Students.clear();
                Schools.clear();
                nStudents = 0;
                nSchools = 0;
                newMatch.reset(Students, Schools);
                System.out.println("\nDatabase cleared!");
            }
            else if(choice.equalsIgnoreCase("Q")){
                break;
            }
            else{
                System.out.println("\nERROR: Invalid menu choice!");
            }
            System.out.println("");
        }
        System.out.print("\nArrivederci!");
    }
}



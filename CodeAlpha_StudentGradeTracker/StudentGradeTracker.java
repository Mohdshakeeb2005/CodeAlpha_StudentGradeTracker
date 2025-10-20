import java.io.*;
import java.util.*;

// -------------------------
// STUDENT CLASS
// -------------------------
class Student {
    String name;
    int rollNumber;
    LinkedHashMap<String, Double> subjects; // subject -> marks

    Student(String name, int rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.subjects = new LinkedHashMap<>();
    }

    void addSubjectMark(String subject, double mark) {
        subjects.put(subject, mark);
    }

    double calculateAverage() {
        if (subjects.isEmpty()) return 0.0;
        double total = 0;
        for (double m : subjects.values()) total += m;
        return total / subjects.size();
    }

    String getGrade() {
        double avg = calculateAverage();
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F";
    }

    @Override
    public String toString() {
        return "Roll No: " + rollNumber + " | Name: " + name +
               " | Avg: " + String.format("%.2f", calculateAverage()) +
               " | Grade: " + getGrade();
    }

    String getSubjectDetails() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> e : subjects.entrySet()) {
            sb.append("\n   → ").append(e.getKey()).append(": ").append(e.getValue());
        }
        return sb.toString();
    }
}

// -------------------------
// GRADE TRACKER CLASS
// -------------------------
class GradeTracker {
    List<Student> students = new ArrayList<>();
    final String FILE_PATH = "grades.txt";

    GradeTracker() {
        loadFromFile();
    }

    void addStudent(String name, int roll) {
        students.add(new Student(name, roll));
        saveToFile();
    }

    Student findStudent(int roll) {
        for (Student s : students) {
            if (s.rollNumber == roll) return s;
        }
        return null;
    }

    void addMarks(int roll, Map<String, Double> subjects) {
        Student s = findStudent(roll);
        if (s != null) {
            for (Map.Entry<String, Double> e : subjects.entrySet()) {
                s.addSubjectMark(e.getKey(), e.getValue());
            }
            System.out.println("✅ Marks added successfully!");
            saveToFile();
        } else {
            System.out.println("⚠️ Student not found!");
        }
    }

    void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\nNo students found!");
            return;
        }
        System.out.println("\n📊 Student Performance Report:");
        for (Student s : students) {
            System.out.println(s);
            System.out.println(s.getSubjectDetails());
            System.out.println("-----------------------------");
        }
    }

    void viewTopper() {
        if (students.isEmpty()) {
            System.out.println("\nNo students available!");
            return;
        }
        Student topper = students.get(0);
        for (Student s : students) {
            if (s.calculateAverage() > topper.calculateAverage()) topper = s;
        }
        System.out.println("\n🏆 Top Performer:");
        System.out.println(topper);
        System.out.println(topper.getSubjectDetails());
    }

    // -------------------------
    // FILE HANDLING
    // -------------------------
    void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student s : students) {
                bw.write(s.rollNumber + "," + s.name);
                for (Map.Entry<String, Double> e : s.subjects.entrySet()) {
                    bw.write("," + e.getKey() + ":" + e.getValue());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error saving data: " + e.getMessage());
        }
    }

    void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Student s = new Student(parts[1], Integer.parseInt(parts[0]));
                    for (int i = 2; i < parts.length; i++) {
                        String[] sub = parts[i].split(":");
                        if (sub.length == 2) {
                            s.addSubjectMark(sub[0], Double.parseDouble(sub[1]));
                        }
                    }
                    students.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error loading data: " + e.getMessage());
        }
    }
}

// -------------------------
// MAIN CLASS
// -------------------------
public class StudentGradeTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GradeTracker tracker = new GradeTracker();

        int choice;
        do {
            System.out.println("\n=======================================");
            System.out.println("🎓 STUDENT GRADE TRACKER SYSTEM (ADVANCED)");
            System.out.println("=======================================");
            System.out.println("1. Add Student");
            System.out.println("2. Add Subjects & Marks");
            System.out.println("3. View All Students");
            System.out.println("4. View Top Performer");
            System.out.println("5. Exit");
            System.out.println("=======================================");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Roll Number: ");
                    int roll = sc.nextInt();
                    tracker.addStudent(name, roll);
                    System.out.println("✅ Student added successfully!");
                    break;

                case 2:
                    System.out.print("Enter Roll Number: ");
                    int rollNo = sc.nextInt();
                    System.out.print("How many subjects? ");
                    int count = sc.nextInt();
                    sc.nextLine();
                    Map<String, Double> subjects = new LinkedHashMap<>();
                    for (int i = 1; i <= count; i++) {
                        System.out.print("Enter subject " + i + " name: ");
                        String sub = sc.nextLine();
                        System.out.print("Enter marks for " + sub + ": ");
                        double marks = sc.nextDouble();
                        sc.nextLine();
                        subjects.put(sub, marks);
                    }
                    tracker.addMarks(rollNo, subjects);
                    break;

                case 3:
                    tracker.viewAllStudents();
                    break;

                case 4:
                    tracker.viewTopper();
                    break;

                case 5:
                    System.out.println("\n💾 Saving data and exiting... Goodbye!");
                    break;

                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        } while (choice != 5);

        sc.close();
    }
}

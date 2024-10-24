import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Domain Model
class Student {
    private String id;
    private String name;
    private int age;

    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

// Data Access Layer
interface StudentDao {
    void addStudent(Student student);
    void updateStudent(Student student);
    void removeStudent(String id);
    List<Student> getAllStudents();
}

class InMemoryStudentDao implements StudentDao {
    private Map<String, Student> studentDatabase = new HashMap<>();

    @Override
    public void addStudent(Student student) {
        studentDatabase.put(student.getId(), student);
    }

    @Override
    public void updateStudent(Student student) {
        studentDatabase.put(student.getId(), student);
    }

    @Override
    public void removeStudent(String id) {
        studentDatabase.remove(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentDatabase.values());
    }
}

// Service Layer
class StudentService {
    private StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void addStudent(String id, String name, int age) {
        Student student = new Student(id, name, age);
        studentDao.addStudent(student);
    }

    public void updateStudent(String id, String name, int age) {
        Student student = new Student(id, name, age);
        studentDao.updateStudent(student);
    }

    public void removeStudent(String id) {
        studentDao.removeStudent(id);
    }

    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }
}

// Presentation Layer
abstract class MenuItem {
    public abstract void execute(StudentService studentService);
}

class ShowStudentsMenuItem extends MenuItem {
    @Override
    public void execute(StudentService studentService) {
        List<Student> students = studentService.getAllStudents();
        for (Student student : students) {
            System.out.println(student);
        }
    }
}

class CreateStudentMenuItem extends MenuItem {
    @Override
    public void execute(StudentService studentService) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student ID:");
        String id = scanner.nextLine();
        System.out.println("Enter student name:");
        String name = scanner.nextLine();
        System.out.println("Enter student age:");
        int age = Integer.parseInt(scanner.nextLine());
        studentService.addStudent(id, name, age);
    }
}

class UpdateStudentMenuItem extends MenuItem {
    @Override
    public void execute(StudentService studentService) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student ID to update:");
        String id = scanner.nextLine();
        System.out.println("Enter new name:");
        String name = scanner.nextLine();
        System.out.println("Enter new age:");
        int age = Integer.parseInt(scanner.nextLine());
        studentService.updateStudent(id, name, age);
    }
}

class RemoveStudentMenuItem extends MenuItem {
    @Override
    public void execute(StudentService studentService) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student ID to remove:");
        String id = scanner.nextLine();
        studentService.removeStudent(id);
    }
}

// Main Application
public class StudentManagementApp {
    public static void main(String[] args) {
        StudentDao studentDao = new InMemoryStudentDao();
        StudentService studentService = new StudentService(studentDao);
        Scanner scanner = new Scanner(System.in);
        MenuItem[] menuItems = {
            new CreateStudentMenuItem(),
            new UpdateStudentMenuItem(),
            new RemoveStudentMenuItem(),
            new ShowStudentsMenuItem()
        };

        while (true) {
            System.out.println("Menu:");
            System.out.println("0: Exit");
            for (int i = 0; i < menuItems.length; i++) {
                System.out.println((i + 1) + ": " + menuItems[i].getClass().getSimpleName());
            }
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                break;
            } else if (choice > 0 && choice <= menuItems.length) {
                menuItems[choice - 1].execute(studentService);
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("You can type \"exit\" for exit");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Type source directory path: ");
                String srcUsrInput = scanner.nextLine();
                if (srcUsrInput.toLowerCase().equals("exit"))
                    break;
                Path srcDirPath = Path.of(srcUsrInput);
                if (isSrcDirValid(srcDirPath)) {
                    System.out.print("Type destination directory path: ");
                    String dstUsrInput = scanner.nextLine();
                    Path dstDirPath = Path.of(dstUsrInput);
                    if (isDstDirValid(dstDirPath)) {
                        copyFile(srcDirPath, dstDirPath);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isSrcDirValid(Path srcDir) {
        if (!Files.isReadable(srcDir)) {
            System.err.println("Source directory was not found or access denied");
            return false;
        }
        if (!Files.isDirectory(srcDir)) {
            System.err.println("Not a directory");
            return false;
        }
        return true;
    }

    private static boolean isDstDirValid(Path dstDirPath) {
        if (Files.exists(dstDirPath) && !Files.isWritable(dstDirPath) || !Files.exists(dstDirPath) && !Files.isWritable(dstDirPath.getParent())) {
            System.err.println("Destination path is not accessible");
            return false;
        }
        return true;
    }

    private static void copyFile(Path srcFilePath, Path dstFilePath) throws IOException {
        if (srcFilePath.equals(dstFilePath)) {
            System.err.println("Can't copy directory to itself");
            return;
        }
        List<Path> pathsList = Files.walk(srcFilePath).collect(Collectors.toList());
        pathsList.forEach(f -> {
            try {
                Files.copy(f, Path.of(dstFilePath.toString() + File.separator + srcFilePath.relativize(f).toString()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        System.out.println("\tDirectory successfully copied");
    }
}

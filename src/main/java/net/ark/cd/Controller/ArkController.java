package net.ark.cd.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ArkController {
    String fileLocation = "/home/leader/Desktop/video/ismail.xlsx";

    @GetMapping("/tox")
    ResponseEntity<InputStreamResource> getDw() {
        try {
            InputStreamResource inputStreamResource = new InputStreamResource(
                    new FileInputStream(fileLocation));
            org.springframework.http.HttpHeaders header = new org.springframework.http.HttpHeaders();
            header.add("Content-Disposition", "attachment; filename=" + UUID.randomUUID().toString() + "AHMED.xlsx");

            return ResponseEntity.ok().headers(header).body(inputStreamResource);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(value = "")
    public String getMethodName() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") String file, RedirectAttributes attributes) {

        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        // return success response
        Path path = Paths.get(file);
        if (Files.exists(path)) {
            attributes.addFlashAttribute("message", "You successfully uploaded ");

            // CREATE FILE EXCEL
            List<Path> filesName = new ArrayList<Path>();
            try (Stream<Path> subpath = Files.walk(path, 1)) {

                subpath.filter(Files::isRegularFile).forEach(p -> filesName.add(p.getFileName()));
            } catch (Exception e) {
            }

            List<String> listofPdf = new ArrayList<>();
            int numberOFpng = 0;
            int numberOFjpg = 0;
            int numberOFmp4 = 0;
            int numberOFexcel = 0;
            int numberOPdf = 0;
            int numberOFtxt = 0;
            int numberOFword = 0;
            for (int i = 0; i < filesName.size(); i++) {

                if (filesName.get(i).toString().contains(".mp4")) {
                    numberOFmp4++;

                }

                if (filesName.get(i).toString().contains(".txt")) {
                    numberOFtxt++;

                }
                if (filesName.get(i).toString().contains(".png")) {
                    numberOFpng++;

                }
                if (filesName.get(i).toString().contains(".pdf")) {
                    numberOPdf++;
                    listofPdf.add(filesName.get(i).getFileName().toString());
                }
                if (filesName.get(i).toString().contains(".doc")) {
                    numberOFword++;

                }
                if (filesName.get(i).toString().contains(".jpg") || filesName.get(i).toString().contains(".jpeg")) {
                    numberOFjpg++;
                }
                if (filesName.get(i).toString().contains(".xls") || filesName.get(i).toString().contains(".xlsx")) {
                    numberOFexcel++;
                }
            }

            try {
                // FileInputStream fis = new FileInputStream("");

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("ARK");
                Row row0 = sheet.createRow(0);

                Cell cellExcep = row0.createCell(0);
                cellExcep.setCellValue("NAMES OF FILES");
                Cell cellpdf = row0.createCell(1);
                cellpdf.setCellValue("COUNT :" + numberOPdf);

                int j = 1;
                for (int index = 0; index < listofPdf.size(); index++) {
                    Row row1 = sheet.createRow(j++);

                    row1.createCell(0).setCellValue(listofPdf.get(index));

                }

                // Row row2 = sheet.createRow(1);
                // Cell celltheCount = row2.createCell(1);
                // celltheCount.setCellValue(numberOPdf);

                FileOutputStream outputStream = new FileOutputStream(fileLocation);
                workbook.write(outputStream);
                workbook.close();

            } catch (

            Exception e) {
                System.out.println(e.toString());
            }

            return "redirect:/";
        }
        if (!Files.exists(path)) {
            attributes.addFlashAttribute("messageER", "NOT EXIST");
            return "redirect:/";
        }

        return "redirect:/";
    }
}
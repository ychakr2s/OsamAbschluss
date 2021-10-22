package ma.ousama.abschlussarbeit.controller;

import com.google.gson.Gson;
import ma.ousama.abschlussarbeit.factory.FactoryAlgorithms;
import ma.ousama.abschlussarbeit.model.Context;
import ma.ousama.abschlussarbeit.model.Graph;
import ma.ousama.abschlussarbeit.model.JsonOutput;
import ma.ousama.abschlussarbeit.service.ReadFile.readFile_Graph;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
public class WebAppController {

    public static String UPLOADED_FOLDER = "uploadingDir/";
    private String fileName = "";
    public Graph graph;

    @GetMapping("/")
    public String index() {
        readFile_Graph.deleteFiles(UPLOADED_FOLDER);
        fileName = "";
        return "index";
    }

    @GetMapping("/implementierung")
    public String indexStatus() {
        readFile_Graph.deleteFiles(UPLOADED_FOLDER);
        fileName = "";

        return "implementierung";
    }

    @GetMapping("/references")
    public String reference() {
        return "references";
    }

    @GetMapping("/impressum")
    public String impressum() {
        return "impressum";
    }

    @PostMapping("/upload") //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/uploadStatus";
        }
        if (file.getSize() > 1048576) {
            redirectAttributes.addFlashAttribute("message", "Die Datei ist zu groß: Die Datei muss die Größe 1048.576 kb nicht überschreiten.!!!");
            return "redirect:/uploadStatus";
        }

        try {
            /*
             * This checks whether a directory contains a file or not. If yes than delete everything and add
             * the new File to the directory.
             */
            readFile_Graph.deleteFiles(UPLOADED_FOLDER);

            // Upload the new File to the Directory "uploadingDir"
            byte[] bytes = file.getBytes();

            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            System.out.println("PATH: "+ path.toString());
            Files.write(path, bytes);

            fileName = path.toString();

            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public @ResponseBody
    String yourMethod(@RequestBody ArrayList<String> selected) throws Exception {

        if (fileName.equals("")) {
            return "Bitte laden Sie eine Graph-Datei hoch";
        } else {
            readFile_Graph rd = new readFile_Graph();

            Graph gr = rd.readGraphFromFile(fileName);
            if (gr == null) {
                return "Die Datei konnte nicht eingelesen werden.!! Sehen Sie bitte oben die Anweisungen";
            }
            Context imp = new Context(FactoryAlgorithms.getAlgorithms(selected, gr));
            JsonOutput jso = new JsonOutput(imp.execute());

            Gson gs = new Gson();
            return gs.toJson(jso);
        }
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "indexStatus";
    }
}
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
    private int m;
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

    // this method treat the uploded file
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
            Files.write(path, bytes);

            fileName = path.toString();
            System.out.println("ist hochgeladen");
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }

    // this method display the Graph
    @RequestMapping(value = "/graphZeigen", method = RequestMethod.GET)
    public @ResponseBody
    String graphAnzeigen() throws Exception {

        if (fileName.equals("")) {
            return "Bitte laden Sie eine Graph-Datei hoch";
        } else {

            if (graph == null) {
                return "Die Datei konnte nicht eingelesen werden.!! Sehen Sie bitte oben die Anweisungen";
            }
            Gson gs = new Gson();

            String gsToJson = gs.toJson(graph);
            System.out.println("MyGraph: "+ gsToJson);
            return gsToJson;
        }
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public @ResponseBody
    String yourMethod(@RequestBody ArrayList<String> selected) throws Exception {

        if (fileName.equals("")) {
            return "Bitte laden Sie eine Graph-Datei hoch";
        } else {
            readFile_Graph rd = new readFile_Graph();

//            if (!rd.isSuitable(fileName)) {
//                return "Graph-Datei entspricht nicht den festgelegten Maßstäbe ";
//            }
            Graph gr = rd.readGraphFromFile(fileName);

            if (gr == null) {
                return "Die Datei konnte nicht eingelesen werden.!! Sehen Sie bitte oben die Anweisungen";
            }
            System.out.println("my Selected: " + selected);
            Context imp = new Context(FactoryAlgorithms.getAlgorithms(selected, gr));
            imp.execute();
            JsonOutput jso = new JsonOutput(imp.execute());
            graph = imp.execute().get(0).getGraph();
//            Gson gs = new Gson();
            Gson gs = new Gson();
            System.out.println("my JSON: " + gs.toJson(jso));
            return gs.toJson(jso);
        }
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "indexStatus";
    }
}
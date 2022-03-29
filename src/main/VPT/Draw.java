package VPT;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Draw {

    public static String convertInputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int length;
        while((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    public static void writeToFile(String text, String filename) throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(filename,true));
        wr.append('\n');
        wr.append(text);
        wr.close();
    }


    public static JFrame draw() throws IOException {
        /* считываем файл, если его нет, создаем новый*/
        File file = new File("./file.txt");
        try (InputStream in = new FileInputStream(file)) {
        } catch (FileNotFoundException e) {
            FileOutputStream of = new FileOutputStream(file, false);
        }

        JFrame f = new JFrame();

        /* Поле ввода текста */
        JTextField inputField = new JTextField();
        inputField.setBounds(20,30,350,30);
        f.add(inputField);

        /* Поле вывода сообщений */
        JTextArea outputArea = new JTextArea();
        outputArea.setBounds(20,80,350,60);
        outputArea.setEditable(false);
        f.add(outputArea);

        /* Кнопка добавления строки*/
        JButton inputButton = new JButton("Ввод");
        inputButton.setBounds(20,200,100,30);
        f.add(inputButton);
        inputButton.addActionListener(e -> {
            String inputText = inputField.getText();
            outputArea.setText("");
            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            if (is == null) {
                outputArea.setText("File not found! Error.");
            }
            try {
                int gotLine = 0;
                assert is != null;
                String text = convertInputStreamToString(is);
                String[] textsplitted = text.split("\\r?\\n");
                for (String line: textsplitted) {
                    if (Objects.equals(inputText.toLowerCase(), line.toLowerCase())) {
                        outputArea.setText("Text " + inputText + " already exists in the file.");
                        gotLine = 1;
                        break;
                    }
                }
                if (gotLine != 1) {
                    writeToFile(inputText, "./file.txt");
                }
            } catch (IOException ex) {
                outputArea.setText("File is corrupted! Error.");
            }
        });

        /*Кнопка очистки полей*/
        JButton clearButton = new JButton("Очистить");
        clearButton.setBounds(140,200,100,30);
        f.add(clearButton);
        clearButton.addActionListener(e -> {
            inputField.setText("");
            outputArea.setText("");
        });

        /*Закрыть приложение*/
        JButton closeButton = new JButton("Закрыть");
        closeButton.setBounds(260, 200,100,30);
        f.add(closeButton);
        closeButton.addActionListener(e -> System.exit(0));

        f.setTitle("Тестовое приложение");
        f.setSize(400,300);
        f.setResizable(false);
        f.setLayout(null);
        f.setVisible(true);

        return f;
    }
}

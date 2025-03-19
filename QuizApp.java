import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
// Interface by swing (JFrame)
// Timer For each question
// Trivia API for fetching quesion
// Diffrent categories Questions with difficulties, type of questions (Multiple type or True False Questions)

public class QuizApp extends JFrame implements ActionListener {

    private JLabel questionLabel;
    private JRadioButton[] options;
    private JButton nextButton;
    private ButtonGroup optionGroup;
    private JPanel questionPanel, buttonPanel;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private List<String> questions = new ArrayList<>();
    private List<String[]> choices = new ArrayList<>();
    private List<Integer> correctAnswers = new ArrayList<>();

    public QuizApp() {
        setTitle("Quiz Application");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadQuestionsFromFile("questions.txt");

        questionPanel = new JPanel(new GridLayout(5, 1));
        buttonPanel = new JPanel();

        questionLabel = new JLabel(questions.get(currentQuestionIndex));
        questionPanel.add(questionLabel);

        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton(choices.get(currentQuestionIndex)[i]);
            optionGroup.add(options[i]);
            questionPanel.add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);

        add(questionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadQuestionsFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Ensure correct format
                    questions.add(parts[0]);
                    choices.add(new String[]{parts[1], parts[2], parts[3], parts[4]});
                    correctAnswers.add(Integer.parseInt(parts[5]));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            checkAnswer();
            currentQuestionIndex++;

            if (currentQuestionIndex < questions.size()) {
                updateQuestion();
            } else {
                showResult();
            }
        }
    }

    private void checkAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected() && i == correctAnswers.get(currentQuestionIndex)) {
                score++;
                break;
            }
        }
    }

    private void updateQuestion() {
        questionLabel.setText(questions.get(currentQuestionIndex));
        for (int i = 0; i < 4; i++) {
            options[i].setText(choices.get(currentQuestionIndex)[i]);
        }
        optionGroup.clearSelection();
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this, "Your score: " + score + " out of " + questions.size());
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizApp::new);
    }
}

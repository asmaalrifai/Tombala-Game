package com.mycompany.tambolagame;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
public class MainGameGUI extends javax.swing.JFrame {

    HomeTambolaGUI home = new HomeTambolaGUI();
    private Players players;
    private Set<Integer> usedNumbers = new HashSet<>();// for numer uniqueness only
    private boolean gameStarted = false;
    private boolean gameEnded = false;

    int[][] card1 = {
        {5, -1, 22, -1, 45, -1, 60, 73, -1},
        {-1, 10, -1, 31, 47, 58, 68, -1, -1},
        {-1, 17, 26, 38, -1, -1, -1, 79, 86}
    };
    int[][] card2 = {
        {9, -1, 21, -1, 48, -1, 64, 70, -1},
        {-1, 14, -1, 31, 44, 50, 65, -1, -1},
        {-1, 18, 23, 38, -1, -1, -1, 79, 81}
    };

    
    public MainGameGUI() {
        home.setVisible(true);
    }

    public MainGameGUI(Players players) {
        initComponents();
        this.players = players;
        updatePlayerLabels();
        populatePlayerGrid(jPanel_Player1);  // plese comment if you wnat the choosen cards to display
        populatePlayerGrid(jPanel_Player2);  // plese comment if you wnat the choosen cards to display
//        initializeGame();                            // plese UNcomment if you wnat the choosen cards to display
    }

    
    private void updatePlayerLabels() {
        player1Name.setText(players.getPlayer1Name());
        player2Name.setText(players.getPlayer2Name());
    }

    
    private void showNextRandomNumber() {
        if (!gameStarted) {
            nextNumberBtn.setText("NEXT NUMBER");
            gameStarted = true;
        }

        if (usedNumbers.size() == 89) {
            Text_RandomNumber.setText("DONE");
            nextNumberBtn.setEnabled(false);
            return;
        }

        int randomNumber = generateUniqueRandomNumber();
        Text_RandomNumber.setText(String.valueOf(randomNumber));

        checkAndUpdateTextFieldBackground(jPanel_Player1, randomNumber);
        checkAndUpdateTextFieldBackground(jPanel_Player2, randomNumber);
    }

    
    private int generateUniqueRandomNumber() {
        Random random = new Random();
        int randomNumber;
        do {
            randomNumber = random.nextInt(89) + 1;
        } while (usedNumbers.contains(randomNumber));
        usedNumbers.add(randomNumber);
        return randomNumber;
    }

    
    private void populatePlayerGrid(javax.swing.JPanel jPanel_Player) {
        Random random = new Random();
        Set<Integer> usedNumbers = new HashSet<>();
        MultiLinkedList card = new MultiLinkedList();
        

        
        for (int rowForNull = 0; rowForNull < 3; rowForNull++) {
            for (int i = 0; i < 4; i++) {
                int col;
                do {
                    col = random.nextInt(9);
                } while (usedNumbers.contains(col));
                usedNumbers.add(col);

                javax.swing.JTextField textField = (javax.swing.JTextField) jPanel_Player.getComponent(rowForNull + col * 3);
                textField.setText("-1");
            }

            usedNumbers.clear();
        }
        
        card.populatePlayerGrid();
        
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 3; row++) {
                javax.swing.JTextField textField = (javax.swing.JTextField) jPanel_Player.getComponent(row + col * 3); // Calculate component index

                if (textField.getText().equals("-1")) {
                    textField.setText(null);
                } else {
                    int number;
                    do {
                        switch (col) {
                            case 0:
                                number = random.nextInt(9) + 1;
                                break;
                            default:
                                number = random.nextInt(10) + (col * 10);
                                break;
                        }
                    } while (usedNumbers.contains(number));
                    textField.setText(String.valueOf(number));
                    usedNumbers.add(number);
                }
            }
        }
    }

    
    private void checkAndUpdateTextFieldBackground(javax.swing.JPanel panel, int randomNumber) {
        for (java.awt.Component component : panel.getComponents()) {
            if (component instanceof javax.swing.JTextField) {
                javax.swing.JTextField textField = (javax.swing.JTextField) component;
                if (textField.getText().equals(String.valueOf(randomNumber))) {
                    textField.setBackground(java.awt.Color.GREEN);
                }
            }
        }
        showBingo(jPanel_Player1, jLabel_FirstBingo_Player1, jLabel_SecondBingo_Player1, jLabel_Tambola_Player1);
        showBingo(jPanel_Player2, jLabel_FirstBingo_Player2, jLabel_SecondBingo_Player2, jLabel_Tambola_Player2);

    }

    
    private void showBingo(javax.swing.JPanel jPanel_Player, javax.swing.JLabel jLabel_FirstBingo, javax.swing.JLabel jLabel_SecondBingo, javax.swing.JLabel jLabel_Tambola) {
        if (!gameEnded) {
            int rows = 3;
            int cols = 9;

            int count = 0;
            boolean firstBingo = false;
            boolean secondBingo = false;
            boolean tambola = false;

            
            for (int row = 0; row < rows; row++) {
                count = 0; 
                for (int col = 0; col < cols; col++) {
                    javax.swing.JTextField textField = (javax.swing.JTextField) jPanel_Player.getComponent(row + col * rows);
                    if (textField.getBackground().equals(java.awt.Color.GREEN)) {
                        count++;
                    }
                }
                if (count == 5) {
                    if (!firstBingo) {
                        jLabel_FirstBingo.setText("BINGO");
                        firstBingo = true;
                    } else if (!secondBingo) {
                        jLabel_SecondBingo.setText("BINGO");
                        secondBingo = true;
                    } else {
                        jLabel_Tambola.setText("TAMBOLA");
                        tambola = true;
                    }
                }
            }

            if (tambola) {
                gameEnded = true;
                showEndGameDialog(players.getPlayer1Name(), players.getPlayer2Name());
            }
        }
    }

    private void showEndGameDialog(String playerName1, String playerName2) {
        String result;
        boolean tambolaPlayer1 = jLabel_Tambola_Player1.getText().equals("TAMBOLA");
        boolean tambolaPlayer2 = jLabel_Tambola_Player2.getText().equals("TAMBOLA");
        checkAndUpdateTextFieldBackground(jPanel_Player2, Integer.parseInt(Text_RandomNumber.getText()));

        if (tambolaPlayer1 && tambolaPlayer2) {
            result = "Tie";
        } else if (tambolaPlayer1) {
            result = playerName1 + " Won!";
        } else if (tambolaPlayer2) {
            result = playerName2 + " Won!";
        } else {
            boolean player1Won = playerName1.equals(players.getPlayer1Name()) && !playerName2.equals(players.getPlayer2Name());
            boolean player2Won = playerName2.equals(players.getPlayer2Name()) && !playerName1.equals(players.getPlayer1Name());

            if (player1Won) {
                result = playerName1 + " Won!";
            } else if (player2Won) {
                result = playerName2 + " Won!";
            } else {
                result = "Tie";
            }
        }

        String[] options = {"AGAIN", "HOME", "EXIT"};
        int choice = JOptionPane.showOptionDialog(this,
                result,
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);

        switch (choice) {
            case 0:
                MainGameGUI mainGame = new MainGameGUI(players);
                mainGame.setVisible(true);
                this.dispose();
                break;
            case 1:
                home.setVisible(true);
                this.dispose();
                break;
            case 2: // EXIT
                System.exit(0);
                break;
            default:
                break;
        }
    }

    
    private void setCard(int[][] card, javax.swing.JPanel jPanel_Player) {
        int rows = card.length;
        int cols = card[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                javax.swing.JTextField textField = (javax.swing.JTextField) jPanel_Player.getComponent(row + col * rows);
                if (card[row][col] != -1) {
                    textField.setText(String.valueOf(card[row][col]));
                } else {
                    textField.setText("");
                }
            }
        }
    }

    
    public void initializeGame() {
        setCard(card1, jPanel_Player1);
        setCard(card2, jPanel_Player2);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_Player1 = new javax.swing.JPanel();
        TextPlayer1_0x0 = new javax.swing.JTextField();
        TextPlayer1_1x0 = new javax.swing.JTextField();
        TextPlayer1_2x0 = new javax.swing.JTextField();
        TextPlayer1_1x1 = new javax.swing.JTextField();
        TextPlayer1_0x1 = new javax.swing.JTextField();
        TextPlayer1_2x1 = new javax.swing.JTextField();
        TextPlayer1_1x2 = new javax.swing.JTextField();
        TextPlayer1_0x2 = new javax.swing.JTextField();
        TextPlayer1_2x2 = new javax.swing.JTextField();
        TextPlayer1_1x3 = new javax.swing.JTextField();
        TextPlayer1_0x3 = new javax.swing.JTextField();
        TextPlayer1_2x3 = new javax.swing.JTextField();
        TextPlayer1_0x4 = new javax.swing.JTextField();
        TextPlayer1_1x4 = new javax.swing.JTextField();
        TextPlayer1_2x4 = new javax.swing.JTextField();
        TextPlayer1_0x5 = new javax.swing.JTextField();
        TextPlayer1_1x5 = new javax.swing.JTextField();
        TextPlayer1_2x5 = new javax.swing.JTextField();
        TextPlayer1_2x6 = new javax.swing.JTextField();
        TextPlayer1_1x6 = new javax.swing.JTextField();
        TextPlayer1_0x6 = new javax.swing.JTextField();
        TextPlayer1_0x7 = new javax.swing.JTextField();
        TextPlayer1_2x7 = new javax.swing.JTextField();
        TextPlayer1_1x7 = new javax.swing.JTextField();
        TextPlayer1_2x8 = new javax.swing.JTextField();
        TextPlayer1_1x8 = new javax.swing.JTextField();
        TextPlayer1_0x8 = new javax.swing.JTextField();
        jPanel_Player2 = new javax.swing.JPanel();
        TextPlayer2_1x0 = new javax.swing.JTextField();
        TextPlayer2_2x0 = new javax.swing.JTextField();
        TextPlayer2_0x0 = new javax.swing.JTextField();
        TextPlayer2_0x1 = new javax.swing.JTextField();
        TextPlayer2_1x1 = new javax.swing.JTextField();
        TextPlayer2_2x1 = new javax.swing.JTextField();
        TextPlayer2_0x2 = new javax.swing.JTextField();
        TextPlayer2_1x2 = new javax.swing.JTextField();
        TextPlayer2_2x2 = new javax.swing.JTextField();
        TextPlayer2_0x3 = new javax.swing.JTextField();
        TextPlayer2_1x3 = new javax.swing.JTextField();
        TextPlayer2_2x3 = new javax.swing.JTextField();
        TextPlayer2_0x4 = new javax.swing.JTextField();
        TextPlayer2_1x4 = new javax.swing.JTextField();
        TextPlayer2_2x4 = new javax.swing.JTextField();
        TextPlayer2_0x5 = new javax.swing.JTextField();
        TextPlayer2_1x5 = new javax.swing.JTextField();
        TextPlayer2_2x5 = new javax.swing.JTextField();
        TextPlayer2_2x6 = new javax.swing.JTextField();
        TextPlayer2_1x6 = new javax.swing.JTextField();
        TextPlayer2_0x6 = new javax.swing.JTextField();
        TextPlayer2_2x7 = new javax.swing.JTextField();
        TextPlayer2_1x7 = new javax.swing.JTextField();
        TextPlayer2_0x7 = new javax.swing.JTextField();
        TextPlayer2_0x8 = new javax.swing.JTextField();
        TextPlayer2_1x8 = new javax.swing.JTextField();
        TextPlayer2_2x8 = new javax.swing.JTextField();
        jPanel_StatusPlayer2 = new javax.swing.JPanel();
        jLabel_SecondBingo_Player2 = new javax.swing.JLabel();
        jLabel_FirstBingo_Player2 = new javax.swing.JLabel();
        jLabel_Tambola_Player2 = new javax.swing.JLabel();
        jPanel_StatusPlayer1 = new javax.swing.JPanel();
        jLabel_FirstBingo_Player1 = new javax.swing.JLabel();
        jLabel_SecondBingo_Player1 = new javax.swing.JLabel();
        jLabel_Tambola_Player1 = new javax.swing.JLabel();
        player2Name = new javax.swing.JLabel();
        player1Name = new javax.swing.JLabel();
        nextNumberBtn = new javax.swing.JButton();
        Text_RandomNumber = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_Player1.setBackground(new java.awt.Color(0, 153, 153));

        TextPlayer1_0x0.setEditable(false);
        TextPlayer1_0x0.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x0.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_0x0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_0x0ActionPerformed(evt);
            }
        });

        TextPlayer1_1x0.setEditable(false);
        TextPlayer1_1x0.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x0.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x0ActionPerformed(evt);
            }
        });

        TextPlayer1_2x0.setEditable(false);
        TextPlayer1_2x0.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x0.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x1.setEditable(false);
        TextPlayer1_1x1.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x1ActionPerformed(evt);
            }
        });

        TextPlayer1_0x1.setEditable(false);
        TextPlayer1_0x1.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_2x1.setEditable(false);
        TextPlayer1_2x1.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x2.setEditable(false);
        TextPlayer1_1x2.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x2ActionPerformed(evt);
            }
        });

        TextPlayer1_0x2.setEditable(false);
        TextPlayer1_0x2.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_2x2.setEditable(false);
        TextPlayer1_2x2.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x3.setEditable(false);
        TextPlayer1_1x3.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x3ActionPerformed(evt);
            }
        });

        TextPlayer1_0x3.setEditable(false);
        TextPlayer1_0x3.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_2x3.setEditable(false);
        TextPlayer1_2x3.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_0x4.setEditable(false);
        TextPlayer1_0x4.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x4.setEditable(false);
        TextPlayer1_1x4.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x4ActionPerformed(evt);
            }
        });

        TextPlayer1_2x4.setEditable(false);
        TextPlayer1_2x4.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_0x5.setEditable(false);
        TextPlayer1_0x5.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x5.setEditable(false);
        TextPlayer1_1x5.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x5ActionPerformed(evt);
            }
        });

        TextPlayer1_2x5.setEditable(false);
        TextPlayer1_2x5.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_2x6.setEditable(false);
        TextPlayer1_2x6.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x6.setEditable(false);
        TextPlayer1_1x6.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x6ActionPerformed(evt);
            }
        });

        TextPlayer1_0x6.setEditable(false);
        TextPlayer1_0x6.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_0x7.setEditable(false);
        TextPlayer1_0x7.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_2x7.setEditable(false);
        TextPlayer1_2x7.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x7.setEditable(false);
        TextPlayer1_1x7.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x7ActionPerformed(evt);
            }
        });

        TextPlayer1_2x8.setEditable(false);
        TextPlayer1_2x8.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_2x8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer1_1x8.setEditable(false);
        TextPlayer1_1x8.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_1x8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer1_1x8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer1_1x8ActionPerformed(evt);
            }
        });

        TextPlayer1_0x8.setEditable(false);
        TextPlayer1_0x8.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer1_0x8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel_Player1Layout = new javax.swing.GroupLayout(jPanel_Player1);
        jPanel_Player1.setLayout(jPanel_Player1Layout);
        jPanel_Player1Layout.setHorizontalGroup(
            jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x0, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x0, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x0, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer1_0x8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_1x8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer1_2x8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel_Player1Layout.setVerticalGroup(
            jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_Player1Layout.createSequentialGroup()
                        .addComponent(TextPlayer1_0x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(TextPlayer1_1x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel_Player1Layout.createSequentialGroup()
                        .addComponent(TextPlayer1_0x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(TextPlayer1_1x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TextPlayer1_2x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TextPlayer1_2x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel_Player1Layout.createSequentialGroup()
                        .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addComponent(TextPlayer1_0x8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer1_1x8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_Player1Layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addGroup(jPanel_Player1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TextPlayer1_2x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer1_2x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer1_2x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer1_2x5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer1_2x6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer1_2x7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer1_2x8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 25, Short.MAX_VALUE))))
        );

        jPanel_Player2.setBackground(new java.awt.Color(0, 102, 102));

        TextPlayer2_1x0.setEditable(false);
        TextPlayer2_1x0.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x0.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x0ActionPerformed(evt);
            }
        });

        TextPlayer2_2x0.setEditable(false);
        TextPlayer2_2x0.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x0.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x0.setEditable(false);
        TextPlayer2_0x0.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x0.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x1.setEditable(false);
        TextPlayer2_0x1.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_1x1.setEditable(false);
        TextPlayer2_1x1.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x1ActionPerformed(evt);
            }
        });

        TextPlayer2_2x1.setEditable(false);
        TextPlayer2_2x1.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x2.setEditable(false);
        TextPlayer2_0x2.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_1x2.setEditable(false);
        TextPlayer2_1x2.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x2ActionPerformed(evt);
            }
        });

        TextPlayer2_2x2.setEditable(false);
        TextPlayer2_2x2.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x3.setEditable(false);
        TextPlayer2_0x3.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_1x3.setEditable(false);
        TextPlayer2_1x3.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x3ActionPerformed(evt);
            }
        });

        TextPlayer2_2x3.setEditable(false);
        TextPlayer2_2x3.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x4.setEditable(false);
        TextPlayer2_0x4.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_1x4.setEditable(false);
        TextPlayer2_1x4.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x4ActionPerformed(evt);
            }
        });

        TextPlayer2_2x4.setEditable(false);
        TextPlayer2_2x4.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x5.setEditable(false);
        TextPlayer2_0x5.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_1x5.setEditable(false);
        TextPlayer2_1x5.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x5ActionPerformed(evt);
            }
        });

        TextPlayer2_2x5.setEditable(false);
        TextPlayer2_2x5.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_2x5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_2x5ActionPerformed(evt);
            }
        });

        TextPlayer2_2x6.setEditable(false);
        TextPlayer2_2x6.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_2x6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_2x6ActionPerformed(evt);
            }
        });

        TextPlayer2_1x6.setEditable(false);
        TextPlayer2_1x6.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x6ActionPerformed(evt);
            }
        });

        TextPlayer2_0x6.setEditable(false);
        TextPlayer2_0x6.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x6.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_2x7.setEditable(false);
        TextPlayer2_2x7.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_2x7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_2x7ActionPerformed(evt);
            }
        });

        TextPlayer2_1x7.setEditable(false);
        TextPlayer2_1x7.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x7ActionPerformed(evt);
            }
        });

        TextPlayer2_0x7.setEditable(false);
        TextPlayer2_0x7.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x7.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_0x8.setEditable(false);
        TextPlayer2_0x8.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_0x8.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        TextPlayer2_1x8.setEditable(false);
        TextPlayer2_1x8.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_1x8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_1x8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_1x8ActionPerformed(evt);
            }
        });

        TextPlayer2_2x8.setEditable(false);
        TextPlayer2_2x8.setBackground(new java.awt.Color(204, 204, 204));
        TextPlayer2_2x8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextPlayer2_2x8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextPlayer2_2x8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_Player2Layout = new javax.swing.GroupLayout(jPanel_Player2);
        jPanel_Player2.setLayout(jPanel_Player2Layout);
        jPanel_Player2Layout.setHorizontalGroup(
            jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x0, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x0, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x0, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TextPlayer2_0x8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_1x8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPlayer2_2x8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_Player2Layout.setVerticalGroup(
            jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_Player2Layout.createSequentialGroup()
                        .addComponent(TextPlayer2_0x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(TextPlayer2_1x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Player2Layout.createSequentialGroup()
                        .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel_Player2Layout.createSequentialGroup()
                                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TextPlayer2_0x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_0x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_0x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TextPlayer2_1x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_1x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_1x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TextPlayer2_2x2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_2x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_2x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_2x5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_Player2Layout.createSequentialGroup()
                                .addComponent(TextPlayer2_0x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer2_1x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TextPlayer2_2x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextPlayer2_2x1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel_Player2Layout.createSequentialGroup()
                        .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel_Player2Layout.createSequentialGroup()
                                    .addComponent(TextPlayer2_0x8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(TextPlayer2_1x8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(57, 57, 57))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Player2Layout.createSequentialGroup()
                                    .addGap(109, 109, 109)
                                    .addComponent(TextPlayer2_2x8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel_Player2Layout.createSequentialGroup()
                                    .addComponent(TextPlayer2_0x7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(TextPlayer2_1x7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(57, 57, 57))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Player2Layout.createSequentialGroup()
                                    .addGap(109, 109, 109)
                                    .addComponent(TextPlayer2_2x7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_Player2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel_Player2Layout.createSequentialGroup()
                                    .addComponent(TextPlayer2_0x6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(TextPlayer2_1x6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(57, 57, 57))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Player2Layout.createSequentialGroup()
                                    .addGap(109, 109, 109)
                                    .addComponent(TextPlayer2_2x6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel_Player2Layout.createSequentialGroup()
                                .addComponent(TextPlayer2_0x5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(TextPlayer2_1x5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel_StatusPlayer2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel_SecondBingo_Player2.setBackground(new java.awt.Color(0, 153, 153));
        jLabel_SecondBingo_Player2.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jLabel_SecondBingo_Player2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel_FirstBingo_Player2.setBackground(new java.awt.Color(0, 153, 153));
        jLabel_FirstBingo_Player2.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jLabel_FirstBingo_Player2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel_Tambola_Player2.setBackground(new java.awt.Color(0, 153, 153));
        jLabel_Tambola_Player2.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jLabel_Tambola_Player2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel_StatusPlayer2Layout = new javax.swing.GroupLayout(jPanel_StatusPlayer2);
        jPanel_StatusPlayer2.setLayout(jPanel_StatusPlayer2Layout);
        jPanel_StatusPlayer2Layout.setHorizontalGroup(
            jPanel_StatusPlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_StatusPlayer2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_StatusPlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_SecondBingo_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Tambola_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_FirstBingo_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel_StatusPlayer2Layout.setVerticalGroup(
            jPanel_StatusPlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_StatusPlayer2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel_FirstBingo_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_SecondBingo_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Tambola_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        jPanel_StatusPlayer1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel_FirstBingo_Player1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_FirstBingo_Player1.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jLabel_FirstBingo_Player1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel_SecondBingo_Player1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_SecondBingo_Player1.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jLabel_SecondBingo_Player1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel_Tambola_Player1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel_Tambola_Player1.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        jLabel_Tambola_Player1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel_StatusPlayer1Layout = new javax.swing.GroupLayout(jPanel_StatusPlayer1);
        jPanel_StatusPlayer1.setLayout(jPanel_StatusPlayer1Layout);
        jPanel_StatusPlayer1Layout.setHorizontalGroup(
            jPanel_StatusPlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_StatusPlayer1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_StatusPlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_FirstBingo_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_SecondBingo_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Tambola_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel_StatusPlayer1Layout.setVerticalGroup(
            jPanel_StatusPlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_StatusPlayer1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel_FirstBingo_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel_SecondBingo_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Tambola_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        player2Name.setBackground(new java.awt.Color(255, 102, 51));
        player2Name.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        player2Name.setForeground(new java.awt.Color(0, 102, 102));
        player2Name.setText("PLAYER 2");

        player1Name.setBackground(new java.awt.Color(0, 153, 153));
        player1Name.setFont(new java.awt.Font("Showcard Gothic", 0, 18)); // NOI18N
        player1Name.setForeground(new java.awt.Color(0, 153, 153));
        player1Name.setText("PLAYER 1");

        nextNumberBtn.setFont(new java.awt.Font("Showcard Gothic", 0, 12)); // NOI18N
        nextNumberBtn.setText("START GAME");
        nextNumberBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextNumberBtnActionPerformed(evt);
            }
        });

        Text_RandomNumber.setEditable(false);
        Text_RandomNumber.setBackground(new java.awt.Color(255, 255, 255));
        Text_RandomNumber.setFont(new java.awt.Font("Showcard Gothic", 1, 24)); // NOI18N
        Text_RandomNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Text_RandomNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Text_RandomNumberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel_Player2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_Player1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel_StatusPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(player1Name)))
                        .addGap(137, 137, 137)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Text_RandomNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(nextNumberBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(player2Name, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel_StatusPlayer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_Player1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(Text_RandomNumber))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(player1Name))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel_StatusPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 8, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(player2Name, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(nextNumberBtn)
                                .addGap(10, 10, 10))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel_StatusPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Player2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Text_RandomNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Text_RandomNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Text_RandomNumberActionPerformed

    private void TextPlayer1_1x0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x0ActionPerformed

    private void TextPlayer1_1x1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x1ActionPerformed

    private void TextPlayer1_1x2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x2ActionPerformed

    private void TextPlayer1_1x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x3ActionPerformed

    private void TextPlayer1_1x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x4ActionPerformed

    private void TextPlayer1_1x5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x5ActionPerformed

    private void TextPlayer1_1x6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x6ActionPerformed

    private void TextPlayer1_1x7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x7ActionPerformed

    private void TextPlayer2_1x0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x0ActionPerformed

    private void TextPlayer2_1x1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x1ActionPerformed

    private void nextNumberBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextNumberBtnActionPerformed
        // TODO add your handling code here:
        showNextRandomNumber();
    }//GEN-LAST:event_nextNumberBtnActionPerformed

    private void TextPlayer2_1x2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x2ActionPerformed

    private void TextPlayer2_1x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x3ActionPerformed

    private void TextPlayer2_1x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x4ActionPerformed

    private void TextPlayer2_1x5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x5ActionPerformed

    private void TextPlayer2_2x5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_2x5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_2x5ActionPerformed

    private void TextPlayer2_2x6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_2x6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_2x6ActionPerformed

    private void TextPlayer2_1x6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x6ActionPerformed

    private void TextPlayer2_2x7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_2x7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_2x7ActionPerformed

    private void TextPlayer2_1x7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x7ActionPerformed

    private void TextPlayer1_1x8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_1x8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_1x8ActionPerformed

    private void TextPlayer2_1x8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_1x8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_1x8ActionPerformed

    private void TextPlayer2_2x8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer2_2x8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer2_2x8ActionPerformed

    private void TextPlayer1_0x0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextPlayer1_0x0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextPlayer1_0x0ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGameGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGameGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGameGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGameGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGameGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TextPlayer1_0x0;
    private javax.swing.JTextField TextPlayer1_0x1;
    private javax.swing.JTextField TextPlayer1_0x2;
    private javax.swing.JTextField TextPlayer1_0x3;
    private javax.swing.JTextField TextPlayer1_0x4;
    private javax.swing.JTextField TextPlayer1_0x5;
    private javax.swing.JTextField TextPlayer1_0x6;
    private javax.swing.JTextField TextPlayer1_0x7;
    private javax.swing.JTextField TextPlayer1_0x8;
    private javax.swing.JTextField TextPlayer1_1x0;
    private javax.swing.JTextField TextPlayer1_1x1;
    private javax.swing.JTextField TextPlayer1_1x2;
    private javax.swing.JTextField TextPlayer1_1x3;
    private javax.swing.JTextField TextPlayer1_1x4;
    private javax.swing.JTextField TextPlayer1_1x5;
    private javax.swing.JTextField TextPlayer1_1x6;
    private javax.swing.JTextField TextPlayer1_1x7;
    private javax.swing.JTextField TextPlayer1_1x8;
    private javax.swing.JTextField TextPlayer1_2x0;
    private javax.swing.JTextField TextPlayer1_2x1;
    private javax.swing.JTextField TextPlayer1_2x2;
    private javax.swing.JTextField TextPlayer1_2x3;
    private javax.swing.JTextField TextPlayer1_2x4;
    private javax.swing.JTextField TextPlayer1_2x5;
    private javax.swing.JTextField TextPlayer1_2x6;
    private javax.swing.JTextField TextPlayer1_2x7;
    private javax.swing.JTextField TextPlayer1_2x8;
    private javax.swing.JTextField TextPlayer2_0x0;
    private javax.swing.JTextField TextPlayer2_0x1;
    private javax.swing.JTextField TextPlayer2_0x2;
    private javax.swing.JTextField TextPlayer2_0x3;
    private javax.swing.JTextField TextPlayer2_0x4;
    private javax.swing.JTextField TextPlayer2_0x5;
    private javax.swing.JTextField TextPlayer2_0x6;
    private javax.swing.JTextField TextPlayer2_0x7;
    private javax.swing.JTextField TextPlayer2_0x8;
    private javax.swing.JTextField TextPlayer2_1x0;
    private javax.swing.JTextField TextPlayer2_1x1;
    private javax.swing.JTextField TextPlayer2_1x2;
    private javax.swing.JTextField TextPlayer2_1x3;
    private javax.swing.JTextField TextPlayer2_1x4;
    private javax.swing.JTextField TextPlayer2_1x5;
    private javax.swing.JTextField TextPlayer2_1x6;
    private javax.swing.JTextField TextPlayer2_1x7;
    private javax.swing.JTextField TextPlayer2_1x8;
    private javax.swing.JTextField TextPlayer2_2x0;
    private javax.swing.JTextField TextPlayer2_2x1;
    private javax.swing.JTextField TextPlayer2_2x2;
    private javax.swing.JTextField TextPlayer2_2x3;
    private javax.swing.JTextField TextPlayer2_2x4;
    private javax.swing.JTextField TextPlayer2_2x5;
    private javax.swing.JTextField TextPlayer2_2x6;
    private javax.swing.JTextField TextPlayer2_2x7;
    private javax.swing.JTextField TextPlayer2_2x8;
    private javax.swing.JTextField Text_RandomNumber;
    private javax.swing.JLabel jLabel_FirstBingo_Player1;
    private javax.swing.JLabel jLabel_FirstBingo_Player2;
    private javax.swing.JLabel jLabel_SecondBingo_Player1;
    private javax.swing.JLabel jLabel_SecondBingo_Player2;
    private javax.swing.JLabel jLabel_Tambola_Player1;
    private javax.swing.JLabel jLabel_Tambola_Player2;
    private javax.swing.JPanel jPanel_Player1;
    private javax.swing.JPanel jPanel_Player2;
    private javax.swing.JPanel jPanel_StatusPlayer1;
    private javax.swing.JPanel jPanel_StatusPlayer2;
    private javax.swing.JButton nextNumberBtn;
    private javax.swing.JLabel player1Name;
    private javax.swing.JLabel player2Name;
    // End of variables declaration//GEN-END:variables
}

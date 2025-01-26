package com.mycompany.tambolagame;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class MultiLinkedList {

    private Node head;

    public MultiLinkedList() {
        this.head = null;
    }

    public void addNode(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }

        Node temp = head;
        while (temp.down != null) {
            temp = temp.down;
        }
        temp.down = newNode;
    }

    
    public void populatePlayerGrid() {
        Random random = new Random();
        Set<Integer> usedNumbers = new HashSet<>();

        Node currentNode = head;
        while (currentNode != null) {
            Node rowNode = currentNode;
            for (int i = 0; i < 5; i++) {
                int number;
                do {
                    number = random.nextInt(90) + 1; // Ensure numbers are between 1 and 90
                } while (usedNumbers.contains(number));
                usedNumbers.add(number);

                rowNode.data = number;

                if (rowNode.right == null && i < 4) {
                    rowNode.right = new Node(0);  // Initialize next right node
                }
                rowNode = rowNode.right;
            }
            currentNode = currentNode.down;
        }
    }
    
    public Node getHead() {
        return head;
    }

    
    public boolean isSimilar(MultiLinkedList list1, MultiLinkedList list2) {
        Node temp1 = list1.head;
        Node temp2 = list2.head;

        while (temp1 != null && temp2 != null) {
            if (temp1.data != temp2.data) {
                return false;
            }
            temp1 = temp1.down;
            temp2 = temp2.down;
        }

        return temp1 == null && temp2 == null; 
    }

    
    public void convertArrayToLinkedList(int[][] array) {
        head = new Node(array[0][0]);

        Node currentRowNode = head;
        Node prevRowNode = null;

        for (int i = 0; i < array.length; i++) {
            Node temp = currentRowNode;

            for (int j = 0; j < array[i].length; j++) {
                temp.right = new Node(array[i][j]);
                temp = temp.right;

                if (prevRowNode != null) {
                    prevRowNode.down = temp;
                    prevRowNode = prevRowNode.right;
                }
            }

            if (i < array.length - 1) {
                currentRowNode = currentRowNode.down;
                prevRowNode = currentRowNode;
            }
        }
    }

    public boolean findNumber(MultiLinkedList list, int target) {
        Node temp = list.head;

        while (temp != null) {
            if (temp.data == target) {
                return true;
            }
            temp = temp.down;
        }

        return false;
    }

    
}

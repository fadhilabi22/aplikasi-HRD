/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import form.LoginForm;
import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
/**
 *
 * @author fadhil abi
 */
public class Main {
    public static void main(String[] args) {
        // 2. Menjalankan Form Login
        SwingUtilities.invokeLater(() -> {
            LoginForm formLogin = new LoginForm();
            formLogin.setVisible(true); // Nampilin form-nya ke layar
        });
        
    }
}
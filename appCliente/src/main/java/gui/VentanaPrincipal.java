package gui;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import resources.Constantes;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import modelo.Contacto;
import modelo.Conversacion;
import modelo.Mensaje;

/**
 *
 * @author Usuario
 */

public class VentanaPrincipal extends javax.swing.JFrame implements IVista{
    private ActionListener controlador;
    private IFormulario registro;
    private IFormulario agregarContacto;
    private IFormulario agregarConversacion;
    private IFormulario editarContacto;
    private String nicknameActivo = null;
    private SideBar sideBar;
    private boolean barraDeMensajeClikeada = false;

    public enum SideBar {
        AGENDA,
        CONVERSACIONES;
    }
   
    public VentanaPrincipal(ActionListener controlador) {
        initComponents();
        hacerVisible(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/resources/iconApp.png")).getImage());
        this.controlador = controlador;
        botonEnviarMensaje.addActionListener(controlador);
        botonAgenda.addActionListener(controlador);
        botonChats.addActionListener(controlador);
        botonAgregarContacto.addActionListener(controlador);
        botonAgregarConversacion.addActionListener(controlador);
    }
    
     /** 
     * Muestra en el chatBody la conversacion con un contacto
     * @param mensajes: lista de mensajes de la conversacion que se quiere mostrar
     * @param nickname: nickname del contacto con el que se tiene la conversacion a ser mostrada
     */
    public void mostrarConversacion(ArrayList<Mensaje> mensajes, String nickname)
    {
        String hora;
        chatBody.removeAll();
        chatBody.revalidate();
        chatBody.repaint();
        nicknameConversacion.setText(nickname);
        if(mensajes != null)
            for(Mensaje mensaje: mensajes)
            {
                hora = mensaje.getFechaHora().substring(11,16);
                if(mensaje.esMio())
                    this.agregarMensaje(mensaje.getContenido(),true,hora);
                else
                    this.agregarMensaje(mensaje.getContenido(),false,hora);
            }
    }
    
    
    public void notificar()
    {
        if(sideBar == SideBar.AGENDA)
        {
           botonChats.setBackground(Constantes.COLOR_NOTIFICACION);
           botonChats.setForeground(Color.BLACK);
           botonChats.setText("CHATS *");
        }
        else
        {
           ActionEvent event = new ActionEvent(botonChats, ActionEvent.ACTION_PERFORMED, "MOVER A CONVERSACIONES");
           controlador.actionPerformed(event);
        }
    }
    
    /**
     * En base a un contenido y a quien pertenece el mensaje, agrega el mensaje visualmente al chatBody
     * @param contenido: contenido del mensaje a agregar
     * @param esMio: booleano que define si el mensaje pertenece al usuario registrado o al contacto
     */
    public void agregarMensaje(String contenido,boolean esMio, String hora)
    {
        ContenedorMensajeVista mensaje =  new ContenedorMensajeVista(contenido,esMio,hora);
        chatBody.add(mensaje);
        chatBody.revalidate();
        chatBody.repaint();
        scrollChatBody.getVerticalScrollBar().setValue(scrollChatBody.getVerticalScrollBar().getMaximum());
    }
    
    /**
     * Agrega en menuList el item recibido por parametro (podria ser un contactoItem o una conversacionItem)
     * @param item: item a ser agregado
     */
    public void agregarMenuItemList(MenuItemList item) 
    {
        menuList.add(item);
        menuList.revalidate();
        menuList.repaint();
    }
    
    /**
     * Carga en el menuList visualmente a los contactos del usuario registrado
     * @param contactos 
     */
    public void cargarContactos(ArrayList<Contacto> contactos) 
    {
        menuList.removeAll();
        menuList.revalidate();
        menuList.repaint();
        botonAgregarConversacion.setEnabled(false);
        botonAgregarContacto.setEnabled(true);
        botonAgenda.setBorder(new SoftBevelBorder(BevelBorder.RAISED,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_NOTIFICACION,Constantes.COLOR_BOTON));
        botonChats.setBorder(new SoftBevelBorder(BevelBorder.RAISED,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON));
        ContactoItemList contactoItemList;
        String nickname;
        for (Contacto contacto: contactos) {
            this.agregarContacto(contacto);
        }
    }
    
    /**
     * Carga en el menuList visualmente a las conversaciones del usuario registrado
     * @param conversaciones 
     */
    public void cargarConversaciones(ArrayList<Conversacion> conversaciones)
    {
        menuList.removeAll();
        menuList.revalidate();
        menuList.repaint();
        botonAgregarConversacion.setEnabled(true);
        botonAgregarContacto.setEnabled(false);
        botonAgenda.setBorder(new SoftBevelBorder(BevelBorder.RAISED,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON));
        botonChats.setBorder(new SoftBevelBorder(BevelBorder.RAISED,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_NOTIFICACION,Constantes.COLOR_BOTON));
        botonChats.setBackground(Constantes.COLOR_BOTON);
        botonChats.setForeground(Color.WHITE);
        botonChats.setText("CHATS");
        for (Conversacion conversacion: conversaciones) {
            this.agregarConversacion(conversacion);
        }
    }
    
    /**
     * Carga en el menuList visualmente a un contacto en particular
     * @param contacto 
     */
    public void agregarContacto(Contacto contacto)
    {
        ContactoItemList contactoItemList;
        String nicknameAgendado,nicknameReal;
        nicknameAgendado = contacto.getNicknameAgendado();
        nicknameReal = contacto.getNicknameReal();
        if (nicknameAgendado.length() >= 20) {
            nicknameAgendado = nicknameAgendado.substring(0,19) + "...";
        }
        if (nicknameReal.length() >= 20) {
            nicknameReal = nicknameReal.substring(0,19) + "...";
        }
        contactoItemList = new ContactoItemList(controlador,nicknameAgendado,nicknameReal);
        agregarMenuItemList(contactoItemList);
    }
    
    /**
     * Actualiza visualmente los datos que el usuario modifico de un contacto en especifico
     * @param nicknameViejo nickname anterior a que lo editen
     * @param contacto contacto con los datos actualizados
     */
    public void actualizarContacto(String nicknameViejo, Contacto contacto)
    {
        String nickname;
        for(Component c: menuList.getComponents())
        {
             ContactoItemList contactoVista = (ContactoItemList) c;
             if(contactoVista.getNickname().equals(nicknameViejo))
             {
                 nickname = contacto.getNicknameAgendado();
                if (nickname.length() >= 20) {
                    nickname = nickname.substring(0,19) + "...";
                }
                contactoVista.setNickname(nickname);
                break;
             }
        }
    }
    
    /**
     * Carga en el menuList visualmente a una conversacion en particular
     * @param conversacion 
     */ 
    public void agregarConversacion(Conversacion conversacion) 
    {
        Contacto contacto;
        ConversacionItemList conversacionItemList;
        contacto = conversacion.getContacto();
        conversacionItemList = new ConversacionItemList(controlador, contacto.getNicknameAgendado(), contacto.getNicknameReal());

        String ultimoMensaje = conversacion.getUltimoMensaje();

        if (ultimoMensaje.length() > 30) {
            ultimoMensaje = ultimoMensaje.substring(0, 30) + "...";
        }
        conversacionItemList.getUltimoMensajeLabel().setText(ultimoMensaje);

        if (conversacion.tieneNotificacion()) {
            conversacionItemList.setNotificacion();
        }
        agregarMenuItemList(conversacionItemList);
}
  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barra = new javax.swing.JPanel();
        nicknameUsuario = new javax.swing.JLabel();
        menu = new javax.swing.JPanel();
        menuBotonesAbajo = new javax.swing.JPanel();
        botonAgregarContacto = new javax.swing.JButton();
        botonAgregarConversacion = new javax.swing.JButton();
        contenedorBotonesMenu = new javax.swing.JPanel();
        botonChats = new javax.swing.JButton();
        botonAgenda = new javax.swing.JButton();
        scrollMenuList = new javax.swing.JScrollPane();
        menuList = new javax.swing.JPanel();
        chat = new javax.swing.JPanel();
        barraDeChat = new javax.swing.JPanel();
        nicknameConversacion = new javax.swing.JLabel();
        contenedorEnviarMsj = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        botonEnviarMensaje = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        ScrollPaneMensaje = new javax.swing.JScrollPane();
        textAreaMensaje = new javax.swing.JTextArea();
        scrollChatBody = new javax.swing.JScrollPane();
        chatBody = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Messenger - Ventana Principal");
        setMinimumSize(new java.awt.Dimension(1000, 650));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        barra.setBackground(Constantes.COLOR_BASE);

        nicknameUsuario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nicknameUsuario.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout barraLayout = new javax.swing.GroupLayout(barra);
        barra.setLayout(barraLayout);
        barraLayout.setHorizontalGroup(
            barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barraLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(nicknameUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(823, Short.MAX_VALUE))
        );
        barraLayout.setVerticalGroup(
            barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nicknameUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(barra, java.awt.BorderLayout.PAGE_START);

        menu.setLayout(new java.awt.BorderLayout());

        menuBotonesAbajo.setBackground(Constantes.COLOR_BASE);

        botonAgregarContacto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/agregarContacto.png"))); // NOI18N
        botonAgregarContacto.setText("agregarconv");
        botonAgregarContacto.setActionCommand("SOLICITUD AGENDAR CONTACTO");
        botonAgregarContacto.setBorder(null);
        botonAgregarContacto.setEnabled(false);
        botonAgregarContacto.setFocusPainted(false);
        botonAgregarContacto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonAgregarContactoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonAgregarContactoMouseExited(evt);
            }
        });

        botonAgregarConversacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/agregarConversacion1.png"))); // NOI18N
        botonAgregarConversacion.setText("agregarconv");
        botonAgregarConversacion.setActionCommand("SOLICITUD AGREGAR CONVERSACION");
        botonAgregarConversacion.setEnabled(false);
        botonAgregarConversacion.setFocusPainted(false);
        botonAgregarConversacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonAgregarConversacionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonAgregarConversacionMouseExited(evt);
            }
        });

        javax.swing.GroupLayout menuBotonesAbajoLayout = new javax.swing.GroupLayout(menuBotonesAbajo);
        menuBotonesAbajo.setLayout(menuBotonesAbajoLayout);
        menuBotonesAbajoLayout.setHorizontalGroup(
            menuBotonesAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuBotonesAbajoLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(botonAgregarConversacion, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(botonAgregarContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        menuBotonesAbajoLayout.setVerticalGroup(
            menuBotonesAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuBotonesAbajoLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(menuBotonesAbajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAgregarConversacion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAgregarContacto))
                .addGap(23, 23, 23))
        );

        menu.add(menuBotonesAbajo, java.awt.BorderLayout.PAGE_END);

        contenedorBotonesMenu.setBackground(Constantes.COLOR_BASE);
        contenedorBotonesMenu.setLayout(new java.awt.GridLayout(1, 2));

        botonChats.setBackground(Constantes.COLOR_BOTON);
        botonChats.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonChats.setForeground(new java.awt.Color(255, 255, 255));
        botonChats.setText("CHATS");
        botonChats.setActionCommand("MOVER A CONVERSACIONES");
        botonChats.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON));
        botonChats.setFocusPainted(false);
        contenedorBotonesMenu.add(botonChats);

        botonAgenda.requestFocusInWindow();
        botonAgenda.setBackground(Constantes.COLOR_BOTON);
        botonAgenda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        botonAgenda.setForeground(new java.awt.Color(255, 255, 255));
        botonAgenda.setText("AGENDA");
        botonAgenda.setActionCommand("MOVER A AGENDA");
        botonAgenda.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON,Constantes.COLOR_BOTON));
        botonAgenda.setFocusPainted(false);
        contenedorBotonesMenu.add(botonAgenda);

        menu.add(contenedorBotonesMenu, java.awt.BorderLayout.PAGE_START);

        JScrollBar scrollBar = new JScrollBar();
        scrollBar.setUI(new ModernScrollBarUI());
        scrollMenuList.setVerticalScrollBar(scrollBar);
        scrollMenuList.setBorder(null);
        scrollMenuList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        menuList.setBackground(new java.awt.Color(47, 52, 52));
        menuList.setLayout(new javax.swing.BoxLayout(menuList, javax.swing.BoxLayout.PAGE_AXIS));
        scrollMenuList.setViewportView(menuList);

        menu.add(scrollMenuList, java.awt.BorderLayout.CENTER);

        getContentPane().add(menu, java.awt.BorderLayout.LINE_START);

        chat.setLayout(new java.awt.BorderLayout());

        barraDeChat.setBackground(Constantes.COLOR_BASE);

        nicknameConversacion.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        nicknameConversacion.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout barraDeChatLayout = new javax.swing.GroupLayout(barraDeChat);
        barraDeChat.setLayout(barraDeChatLayout);
        barraDeChatLayout.setHorizontalGroup(
            barraDeChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barraDeChatLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(nicknameConversacion, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(306, Short.MAX_VALUE))
        );
        barraDeChatLayout.setVerticalGroup(
            barraDeChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, barraDeChatLayout.createSequentialGroup()
                .addComponent(nicknameConversacion, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        chat.add(barraDeChat, java.awt.BorderLayout.PAGE_START);

        contenedorEnviarMsj.setBackground(new java.awt.Color(47, 52, 52));
        contenedorEnviarMsj.setMinimumSize(new java.awt.Dimension(705, 100));
        contenedorEnviarMsj.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(Constantes.COLOR_BASE);

        botonEnviarMensaje.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/enviar(2).png"))); // NOI18N
        botonEnviarMensaje.setActionCommand("ENVIAR MENSAJE");
        botonEnviarMensaje.setAlignmentX(0.5F);
        botonEnviarMensaje.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        botonEnviarMensaje.setFocusPainted(false);
        botonEnviarMensaje.setIconTextGap(0);
        botonEnviarMensaje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonEnviarMensajeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonEnviarMensajeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonEnviarMensajeMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonEnviarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(botonEnviarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        contenedorEnviarMsj.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setBackground(Constantes.COLOR_BASE);

        JScrollBar scrollBarMensaje = new JScrollBar();
        scrollBarMensaje.setUI(new ModernScrollBarUI());
        ScrollPaneMensaje.setVerticalScrollBar(scrollBarMensaje);
        ScrollPaneMensaje.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        textAreaMensaje.setBackground(new java.awt.Color(47, 52, 52));
        textAreaMensaje.setColumns(20);
        textAreaMensaje.setForeground(new java.awt.Color(255, 255, 255));
        textAreaMensaje.setLineWrap(true);
        textAreaMensaje.setRows(5);
        textAreaMensaje.setText("Ingrese su mensaje aqui...");
        textAreaMensaje.setAutoscrolls(false);
        textAreaMensaje.setMargin(new java.awt.Insets(0, 10, 10, 20));
        textAreaMensaje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textAreaMensajeMouseClicked(evt);
            }
        });
        ScrollPaneMensaje.setViewportView(textAreaMensaje);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(ScrollPaneMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addGap(45, 45, 45))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(ScrollPaneMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );

        contenedorEnviarMsj.add(jPanel5, java.awt.BorderLayout.CENTER);

        chat.add(contenedorEnviarMsj, java.awt.BorderLayout.PAGE_END);

        JScrollBar scrollBarChatBody = new JScrollBar();
        scrollBarChatBody.setUI(new ModernScrollBarUI());
        scrollChatBody.setVerticalScrollBar(scrollBarChatBody);
        scrollChatBody.setViewportView(chatBody);
        scrollChatBody.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(30, 30, 30)));

        chatBody.setBackground(Constantes.COLOR_BASE);
        chatBody.setLayout(new javax.swing.BoxLayout(chatBody, javax.swing.BoxLayout.PAGE_AXIS));
        scrollChatBody.setViewportView(chatBody);

        chat.add(scrollChatBody, java.awt.BorderLayout.CENTER);

        getContentPane().add(chat, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public boolean isBarraDeMensajeClickeada() {
        return barraDeMensajeClikeada;
    }
    
    public void hacerVisible(boolean b)
    {
        this.setVisible(b);
    }
    
    public void setNicknameUsuario(String nickname)
    {
        this.nicknameUsuario.setText(nickname);
    }
    
    public String getMensaje()
    {
        return this.textAreaMensaje.getText();
    }

    public String getNicknameRealActivo() {
        return nicknameActivo;
    }

    public void setNicknameRealActivo(String nicknameActivo) {
        this.nicknameActivo = nicknameActivo;
    }
    
    public void setNicknameConversacionActiva(String nickname)
    {
        this.nicknameConversacion.setText(nickname);
    }
    
    public String getNicknameConversacionActiva()
    {
        return nicknameConversacion.getText();
    }
        
    public void setSideBar(SideBar sideBar)
    {
        this.sideBar = sideBar;
    }
    
    public void abrirFormularioRegistro()
    {
        registro = new FormularioRegistro(controlador);
        registro.abrirFormulario();
    }
    
    public String getNicknameRegistro() {
       return registro.getNickname();
    }
    
    public String getIPRegistro()
    {
        return registro.getIP();
    }
    
    public int getPuertoRegistro()
    {
        return registro.getPuerto();
    }
    
    public void abrirFormularioAgregarContacto(ArrayList<String> contactos) 
    {
        this.agregarContacto = new FormularioAgregar(this,true,controlador, "AGENDAR CONTACTO", "Agendar contacto");
        agregarContacto.abrirFormulario(contactos);
    }

    public void cerrarFormularioAgregarContacto() {
       this.agregarContacto.cerrarFormulario();
    }

    public String getNicknameContacto() {
       return this.agregarContacto.getNickname();
    }

    public void abrirFormularioAgregarConversacion(ArrayList<String> contactos) 
    {
       this.agregarConversacion = new FormularioAgregar(this,true,controlador, "AGREGAR CONVERSACION", "Agregar conversacion");
       agregarConversacion.abrirFormulario(contactos);
    }

    public void cerrarFormularioAgregarConversacion() 
    {
        this.agregarConversacion.cerrarFormulario();
    }

    public void cerrarFormularioRegistro() 
    {
       this.registro.cerrarFormulario();
    }
    
    public void abrirFormularioEditarContacto(String nickname)
    {
        this.editarContacto = new FormularioEditarContacto(controlador,nickname);
        editarContacto.abrirFormulario();
    }
    
    public void cerrarFormularioEditarContacto()
    {
        this.editarContacto.cerrarFormulario();
    }
    
    public String getNicknameContactoEditado() {
        return ((FormularioEditarContacto) editarContacto).getNicknameContactoEditado();
    } 
    
    public String getNicknameContactoActual() {
        return editarContacto.getNickname();
    } 
    
    public String getNicknameConversacion() 
    {
      return this.agregarConversacion.getNickname();
    }
    
    public void disableBotonAgregarContacto()
    {
        botonAgregarContacto.setEnabled(false);
    }
    
    public void disableBotonAgregarConversacion()
    {
        botonAgregarConversacion.setEnabled(false);
    }
    
    public void enableBotonAgregarContacto()
    {
        botonAgregarContacto.setEnabled(true);
    }
    
    public void enableBotonAgregarConversacion()
    {
        botonAgregarConversacion.setEnabled(true);
    }
    
    private void textAreaMensajeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textAreaMensajeMouseClicked
        textAreaMensaje.setText("");
        this.barraDeMensajeClikeada = true;
    }//GEN-LAST:event_textAreaMensajeMouseClicked

    private void botonEnviarMensajeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEnviarMensajeMouseClicked
        textAreaMensaje.setText("Ingrese su mensaje aqui...");
        this.barraDeMensajeClikeada = false;
    }//GEN-LAST:event_botonEnviarMensajeMouseClicked

    private void botonAgregarContactoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAgregarContactoMouseEntered
       botonAgregarContacto.setBorder(BorderFactory.createLineBorder(Constantes.COLOR_NOTIFICACION, 1));
    }//GEN-LAST:event_botonAgregarContactoMouseEntered

    private void botonAgregarContactoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAgregarContactoMouseExited
       botonAgregarContacto.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_botonAgregarContactoMouseExited

    private void botonAgregarConversacionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAgregarConversacionMouseEntered
       botonAgregarConversacion.setBorder(BorderFactory.createLineBorder(Constantes.COLOR_NOTIFICACION, 1));
    }//GEN-LAST:event_botonAgregarConversacionMouseEntered

    private void botonAgregarConversacionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonAgregarConversacionMouseExited
       botonAgregarConversacion.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_botonAgregarConversacionMouseExited

    private void botonEnviarMensajeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEnviarMensajeMouseEntered
       botonEnviarMensaje.setBorder(BorderFactory.createLineBorder(Constantes.COLOR_NOTIFICACION, 1));
    }//GEN-LAST:event_botonEnviarMensajeMouseEntered

    private void botonEnviarMensajeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonEnviarMensajeMouseExited
        botonEnviarMensaje.setBorder(BorderFactory.createEmptyBorder());
    }//GEN-LAST:event_botonEnviarMensajeMouseExited

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
       ActionEvent evento = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CIERRE");
       controlador.actionPerformed(evento);
    }//GEN-LAST:event_formWindowClosed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPaneMensaje;
    private javax.swing.JPanel barra;
    private javax.swing.JPanel barraDeChat;
    private javax.swing.JButton botonAgenda;
    private javax.swing.JButton botonAgregarContacto;
    private javax.swing.JButton botonAgregarConversacion;
    private javax.swing.JButton botonChats;
    private javax.swing.JButton botonEnviarMensaje;
    private javax.swing.JPanel chat;
    private javax.swing.JPanel chatBody;
    private javax.swing.JPanel contenedorBotonesMenu;
    private javax.swing.JPanel contenedorEnviarMsj;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel menu;
    private javax.swing.JPanel menuBotonesAbajo;
    private javax.swing.JPanel menuList;
    private javax.swing.JLabel nicknameConversacion;
    private javax.swing.JLabel nicknameUsuario;
    private javax.swing.JScrollPane scrollChatBody;
    private javax.swing.JScrollPane scrollMenuList;
    private javax.swing.JTextArea textAreaMensaje;
    // End of variables declaration//GEN-END:variables
}


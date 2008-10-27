package DamasUNA;


import java.awt.event.*;
import javax.swing.*;

public class Damas extends JFrame {
        private int[][] PosTablero;
       
    
	public Damas() {
		
		super("Juego de Damas");
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		final JDesktopPane mdiDamas = new JDesktopPane();
		getContentPane().add(mdiDamas);
		
		JMenu menuJuego = new JMenu("Juego");
		menuJuego.setMnemonic('J');
		
		
		menuJuego.addSeparator();
//-------->juego local<--------------------------------------		
		             
             JMenuItem AlfaBetaVsMaxmin = new JMenuItem("AlfaBeta vs. MaxMin");
             
             AlfaBetaVsMaxmin.addActionListener(
                     new ActionListener(){
				public void actionPerformed( ActionEvent e){
				                        
                         
                        AlfaBetaVsMaxmin estrategia1= new AlfaBetaVsMaxmin();
                        estrategia1.inicializar(mdiDamas);
                        estrategia1.start();
                
                
                                }
             }       
                   );  
                menuJuego.add(AlfaBetaVsMaxmin);
		menuJuego.addSeparator();
//----------------------------------------------------------------     
                JMenuItem AleatoriavsMinimax = new JMenuItem("Aleatoria vs. MaxMin");
             AleatoriavsMinimax.addActionListener(
                     new ActionListener(){
				public void actionPerformed( ActionEvent e){
				AleatoriovsMaxMin estrategia1= new AleatoriovsMaxMin();
                        estrategia1.inicializar(mdiDamas);
                        estrategia1.start();
                        
                
		
               
                                }
             }       
                   );  
                menuJuego.add(AleatoriavsMinimax);
		menuJuego.addSeparator();
 //----------------------------------------------------------------------------
                JMenuItem AleatoriavsAlfaBeta = new JMenuItem("Aleatoria vs. AlfaBeta");
             AleatoriavsAlfaBeta.addActionListener(
                     new ActionListener(){
				public void actionPerformed( ActionEvent e){
				AleatorioVsAlfaBeta estrategia1= new AleatorioVsAlfaBeta();
                        estrategia1.inicializar(mdiDamas);
                        estrategia1.start();
                        
               
		
                                }
             }       
                   );  
                menuJuego.add(AleatoriavsAlfaBeta);
		menuJuego.addSeparator();
                
//-----------------------------------------------------------------------------
		JMenuItem mItemSalir = new JMenuItem("Salir");
		mItemSalir.setMnemonic('S');
		mItemSalir.addActionListener( 
			new ActionListener(){
				public void actionPerformed( ActionEvent e){
					dispose();
					System.exit(0);
				}
			}
		);
		menuJuego.add(mItemSalir);
		menu.add(menuJuego);	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		show();
	}//termina constructor Damas
//-----------------------------------------------------------------        
public void SetTablero(int [][] posTablero){
         this.PosTablero=posTablero;
}

public int[][] getTablero(){
        return this.PosTablero;
}

public void randomV(){
      int [][] matriz=new int[8][8];
      int z;
        for (int i = 0; i < 8; i++){
            for(int j= 0; j < 8; j++){
             z= (int)(Math.random()*5); 
                if(z==0)z=1; else if(z==1) z=-1;else if(z==2) z=2;else if(z==3)z=-2; else z=0;
               matriz[i][j] = z;
               System.out.println(matriz[i][j]);
            }
        }
      SetTablero(matriz);
      
}

//<<<<<<<<<<<<<<<<<<<<<<<MAIN>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public static void main(String args[]) {
		System.out.println("Iniciando Damas");
		
                
                Damas mainFrame = new Damas();
               
                
		mainFrame.setSize(840, 680);
                mainFrame.setLocation(90,40);
		mainFrame.setVisible(true);
                
                
                
	}
}//termina clase Damas



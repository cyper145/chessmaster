package DamasUNA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TableroGUI extends JInternalFrame 
	implements MouseListener, MouseMotionListener { 
   	private Container container;
   	private GridBagLayout gbLayout;
   	private GridBagConstraints gbConstraints; 
   	private Casa casa[][] = new Casa[8][8];
   	private int tablero[][] = new int[8][8];
    private Color colorA = new Color(26,189,79);
    private Color colorB = new Color(34,87,225);
    private byte piezasA = 0;
    private byte piezasB = 0;
    public boolean bloq = false;
 
    
   	public TableroGUI()
	{

		super("Test",true, true);
		
		int lin;
		int col;

		setTitle( "Juego de Damas" );

		addMouseListener( this );
		addMouseMotionListener( this );
		
		container = getContentPane();
		gbLayout = new GridBagLayout();
		container.setLayout(gbLayout);   
		// instantiate gridbag constraints
		gbConstraints = new GridBagConstraints();
		gbConstraints.fill = GridBagConstraints.BOTH;
		gbConstraints.weightx = 1;
		gbConstraints.weighty = 1;
      	for (lin = 0; lin < 8; lin++)
      	{
			for (col = 0; col < 8; col++)      	
      		{
				//inicializa a casa
      			casa[lin][col] = new Casa();      			
      			addComponent(casa[lin][col],lin,col,1,1);
      		}
        }

      	resetearTablero();
      	
      	posicionaJugador(1,0);
      	posicionaJugador(-1,40);
      		
      	setSize(500,500);
     	
    }

    public void resetearTablero()
    {
	int posX = 0;
      	int posY = 0;
	
	  	//for para definir as posições validas do tablero
      	for (posX = 0; posX < 8; posX++)
      	{
      		for (posY = 0; posY < 8; posY++)
      		{
      			
      			if (posX % 2 == 0)
      			{
      				if (posY % 2 == 0)
      					tablero[posX][posY] = 0;
      				else
      					tablero[posX][posY] = 9;
      			}
      			else
      			{
      				if (posY % 2 != 0)
      					tablero[posX][posY] = 0;
      				else
      					tablero[posX][posY] = 9;
      			}
      			
      			if (tablero[posX][posY] == 9)
      			{
					casa[posX][posY].setBackground(Color.black);
					casa[posX][posY].setForeground(Color.black);
	  			}
      			else
      			{
      				casa[posX][posY].setBackground(Color.white);
      				casa[posX][posY].setForeground(Color.white);
      			}
      		}
      	}
	}

	public void posicionaJugador(int jugador, int posInicial)
	{
		int cont;
		int pos = 0;
		
		if (jugador == 1 || jugador == -1) //A=1 y B=-1
		{
			for (cont = 0; cont < 12; cont++)
			{
				while(pos < 32)
				{
					if (tablero[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)] == 0)// ' '= 0
					{	
						tablero[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)] = jugador;// 1 o -1
						if (jugador == 1)// 1 o -1
						{
							casa[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)].setForeground(colorA);//dibuja la ficha verde
							piezasA++;
						}
						else if (jugador == -1)
						{
							casa[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)].setForeground(colorB);//dibuja la ficha azul
							piezasB++;
						}
						break;
					}
					pos++;
				}
			}
		}
	}

        public void posActual(int [][]PosTablero ){
            
            resetearTablero();
            for(int i=0;i<8;i++){
                    for(int j=0;j<8;j++){
                        if(PosTablero[i][j]==1){
                            casa[i][j].setForeground(colorA);
                        }else if(PosTablero[i][j]==-1){
                            casa[i][j].setForeground(colorB);
                        }
                         if (PosTablero[i][j] == 2){
                        Casa dama=casa[i][j];
                        dama.setDama(true);
                        casa[i][j].setForeground(colorA);
                        }else if(PosTablero[i][j] == -2){
                           Casa dama=casa[i][j];
                        dama.setDama(true);
                        casa[i][j].setForeground(colorB);
                        }
                    }
            }           			
        }
        
	public void muestra(JDesktopPane main)
	{
		main.add(this);
               
        setOpaque(true);
        show();
	}
        public void addComponent(Component c, int linha, int coluna, int width, int height)	{
		gbConstraints.gridx = coluna;
		gbConstraints.gridy = linha;
		
		gbConstraints.gridwidth = width;
		gbConstraints.gridheight = height;
		
		gbLayout.setConstraints(c,gbConstraints);
		container.add(c);
	}




		// botao do mouse foi pressionado e liberado sem mover cursor
   public void mouseClicked( MouseEvent e ){
     // System.out.println( "Clicado em [" + e.getX() +
     //                    ", " + e.getY() + "]" );
   }

   	// botao do mouse foi solto
   public void mouseReleased( MouseEvent e ){
		//int x = 0;	
    }
   public void mousePressed( MouseEvent e ){
      //System.out.println( "Liberado em [" + e.getX() +
      //                   ", " + e.getY() + "]" );
   }

   	// cursor do mouse entra nos limites de um componente
   public void mouseEntered( MouseEvent e ){
      //System.out.println( "Mouse dentro da janela" );
   }

   // cursor do mouse sai dos limites de um componente
   public void mouseExited( MouseEvent e ){
      //System.out.println( "Mouse fora da janela" );
   }

   // botao foi pressionado e mouse movido
   public void mouseDragged( MouseEvent e ){
      //System.out.println( "Arrastado para [" + e.getX() +
      //                   ", " + e.getY() + "]" );
   }

   // mouse foi movido
   public void mouseMoved( MouseEvent e ){
      //System.out.println( "Movido para [" + e.getX() +
      //                   ", " + e.getY() + "]" );
   }
    
}
package DamasUNA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TableroGUI extends JInternalFrame implements MouseListener, MouseMotionListener {

    private Container container;
    private GridBagLayout gbLayout;
    private GridBagConstraints gbConstraints;
    private Casa casa[][] = new Casa[8][8];
    private char tabuleiro[][] = new char[8][8];
    private Color colorA = new Color(26, 189, 79);
    private Color colorB = new Color(34, 87, 225);
    private byte piezasA = 0;
    private byte piezasB = 0;
    public boolean bloq = false;
    private char vez;
    

    public TableroGUI(char vez, String titulo) {

        super("Test", true, true);

        int lin;
        int col;
        this.vez=vez;
        setTitle(titulo);

        addMouseListener(this);
        addMouseMotionListener(this);

        container = getContentPane();
        gbLayout = new GridBagLayout();
        container.setLayout(gbLayout);
        // instantiate gridbag constraints
        gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        for (lin = 0; lin < 8; lin++) {
            for (col = 0; col < 8; col++) {
                //inicializa a casa
                casa[lin][col] = new Casa();
                addComponent(casa[lin][col], lin, col, 1, 1);
            }
        }

        resetearTablero();

        posicionaJugador('A', 0);
        posicionaJugador('B', 40);

        setSize(500, 500);

    }

    public void resetearTablero() {
        	int posX = 0;
      	int posY = 0;
	
	  	//for para definir as posi��es validas do tabuleiro
      	for (posX = 0; posX < 8; posX++)
      	{
      		for (posY = 0; posY < 8; posY++)
      		{
      			
      			if (posX % 2 == 0)
      			{
      				if (posY % 2 == 0)
      					tabuleiro[posX][posY] = ' ';
      				else
      					tabuleiro[posX][posY] = '*';
      			}
      			else
      			{
      				if (posY % 2 != 0)
      					tabuleiro[posX][posY] = ' ';
      				else
      					tabuleiro[posX][posY] = '*';
      			}
      			
      			if (tabuleiro[posX][posY] == '*')
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

    public void posicionaJugador(char jogador, int posInicial) {
        int cont;
		int pos = 0;
		
		if (jogador == 'A' || jogador == 'B')
		{
			for (cont = 0; cont < 12; cont++)
			{
				while(pos < 32)
				{
					if (tabuleiro[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)] == ' ')
					{	
						tabuleiro[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)] = jogador;
						if (jogador == 'A')
						{
							casa[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)].setForeground(this.colorA);
							this.piezasA++;
						}
						else if (jogador == 'B')
						{
							casa[(pos + posInicial) / 8][(int)((pos + posInicial) % 8)].setForeground(this.colorB);
							this.piezasB++;
						}
						break;
					}
					pos++;
				}
			}
		}
    }

    public void muestra(JDesktopPane main) {
        main.add(this);

        setOpaque(true);
        show();
    }

    public void addComponent(Component c, int linha, int coluna, int width, int height) {
        gbConstraints.gridx = coluna;
        gbConstraints.gridy = linha;

        gbConstraints.gridwidth = width;
        gbConstraints.gridheight = height;

        gbLayout.setConstraints(c, gbConstraints);
        container.add(c);
    }
    // botao do mouse foi pressionado e liberado sem mover cursor
    public void mouseClicked(MouseEvent e) {
        // System.out.println( "Clicado em [" + e.getX() +
        //                    ", " + e.getY() + "]" );
    }

    // botao do mouse foi solto
    public void mouseReleased(MouseEvent e) {
        //int x = 0;	
    }

    public void mousePressed(MouseEvent e) {
        //System.out.println( "Liberado em [" + e.getX() +
        //                   ", " + e.getY() + "]" );
    }

    // cursor do mouse entra nos limites de um componente
    public void mouseEntered(MouseEvent e) {
        //System.out.println( "Mouse dentro da janela" );
    }

    // cursor do mouse sai dos limites de um componente
    public void mouseExited(MouseEvent e) {
        //System.out.println( "Mouse fora da janela" );
    }

    // botao foi pressionado e mouse movido
    public void mouseDragged(MouseEvent e) {
        //System.out.println( "Arrastado para [" + e.getX() +
        //                   ", " + e.getY() + "]" );
    }

    // mouse foi movido
    public void mouseMoved(MouseEvent e) {
        //System.out.println( "Movido para [" + e.getX() +
        //                   ", " + e.getY() + "]" );
    }

    public void cambiarTurno() {
        this.vez = (this.vez == 'A' ? 'B' : 'A');
    }

    
    public void moverPieza(int filaO, int columaO, int filaD, int columnaD) {
        if (tabuleiro[filaO][columaO] != ' ') {
            Casa origem = casa[filaO][columaO];
            Casa destino = casa[filaD][columnaD];
            tabuleiro[filaD][columnaD] = tabuleiro[filaO][columaO];
            tabuleiro[filaO][columaO] = ' ';
            destino.setForeground(origem.getForeground());
            origem.setForeground(origem.getBackground());
            if ((filaD == 7 && tabuleiro[filaD][columnaD] == 'A') || (filaD == 0 && tabuleiro[filaD][columnaD] == 'B') || (origem.isDama())) {
                destino.setDama(true);
            }
            casa[filaO][columaO].setDama(false);


        }

    }
    public void comerPieza(int fila, int columna)
    {
		if (tabuleiro[fila][columna] == 'A')
			this.piezasA--;
		else if (tabuleiro[fila][columna] == 'B')
			this.piezasB--;
		tabuleiro[fila][columna] = ' ';
		casa[fila][columna].setDama(false);
		casa[fila][columna].setForeground(casa[fila][columna].getBackground());
		
    }
    public int [][] getIntTable()
    {
        int [][] table= new int [8][8];
        for (int i = 0; i < table.length; i++) 
        {
            for (int j = 0; j < table.length; j++) {
                Casa actual= this.casa[i][j];
                if(this.tabuleiro[i][j]=='A')
                {
                    if(actual.isDama())
                        table[i][j]=2;
                    else
                        table[i][j]=1;
                }else if(this.tabuleiro[i][j]=='B')
                {
                    if(actual.isDama())
                        table[i][j]=-2;
                    else
                        table[i][j]=-1;
                }
                    
                
            }
            
        }
        return table;
    }
}
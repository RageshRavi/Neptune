/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WidgetUtils;

import static com.neptune.common.DataHolder.playListScrollPanel;
import static com.neptune.common.DataHolder.playList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author ragesh.raveendran
 */
public class OnDemandPlaylist extends JFrame{
   
    private	JPanel		topPanel;
	private	JTable		table;
	private	JScrollPane scrollPane;

	// Constructor of main frame
	public OnDemandPlaylist()
	{
		// Set the frame characteristics
		setTitle( "Simple Table Application" );
		setSize( 300, 200 );
		setBackground( Color.gray );

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
                javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(0, 0) {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false;
                            }
                        };
		// Create columns names
		String columnNames[] = { "Column 1", "Column 2", "Column 3", "Column 4"};

		// Create some data
		String dataValues[][] =
		{
			{ "12", "234", "67", "11" },
			{ "-123", "43", "853", "11" },
			{ "93", "89.2", "109", "11" },
			{ "279", "9033", "3092", "11" }
		};

		// Create a new table instance
		table = new JTable( dataValues, columnNames );
                table.setTableHeader(null);

		// Add the table to a scrolling pane
		scrollPane = new JScrollPane( table );
		topPanel.add( scrollPane, BorderLayout.CENTER );
	}

	// Main entry point for this example
	public static void main( String args[] )
	{
		// Create an instance of the test application
		OnDemandPlaylist mainFrame	= new OnDemandPlaylist();
		mainFrame.setVisible( true );
	}
    }

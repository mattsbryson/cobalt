// $Id: ActionDownload.java,v 1.10 2005/03/22 14:42:24 mhaller Exp $



import java.awt.Color;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeModel;

import netscape.javascript.JSObject;

/**
 * The actual download
 * 
 * @author Mike Haller
 * @since 28.01.2004 00:16:46
 */
public class ActionDownload extends Thread implements ActionListener {
	
	//private static final Logger log = Logger.getAnonymousLogger();
	
	boolean stopped = true;
        
        boolean noperm = false;

	private DefaultTreeModel treemodel;

	private DownloadTreeNode root;

	private Manager manager;

	private Border dottedBorder;

	private Border noBorder;

	// constructor
	ActionDownload() {
		// get the manager singleton object
		manager = Manager.getInstance();
		// create the decorated border for the items which are currently in
		// progress
		dottedBorder = BorderFactory.createLineBorder(Color.red);
		noBorder = BorderFactory.createEmptyBorder();
		// set the download thread as low priority, so we can do other stuff
		setPriority(MIN_PRIORITY);
		// run the thing in background
		start();
	}

	/**
	 * runs the download thread
	 */
	public void run() {
		while (true) {
			while (!stopped) {

				Manager.getInstance().getEventDispatcher().fireEvent(
						IEventListener.DOWNLOAD_STARTING, new Object[] {});

				manager.setStatus("Starting downloads..."); //$NON-NLS-1$
				// 1) Get the target location
				File target_location = manager.getHeaderPanel().getTargetLocation();
                                if(target_location.toString().equals(""))
                                {
                                    JSObject window = JSObject.getWindow(Manager.getInstance());
                                    window.eval("alert(\"Please select a local folder first!\")");
                                
                                    manager.getControlPanel().getButtonStop().setEnabled(false);
                                    manager.getControlPanel().getButtonDownload().setEnabled(true);
                                    manager.setStatus("No downloads in progress. Press 'Download' button to start.");
                                    
                                    stopped = true;
                                }
				// 2) Get the first track from tree
				treemodel = Manager.getInstance().getExplorer().getTreeModel();
				root = (DownloadTreeNode) treemodel.getRoot();
				// start the downloads
				recursiveDownload(root, target_location);
				// Enable and disable the buttons
				manager.getControlPanel().getButtonStop().setEnabled(false);
				manager.getControlPanel().getButtonDownload().setEnabled(true);
				// stop the downloads

				if (stopped) {
					Manager.getInstance().getEventDispatcher().fireEvent(
							IEventListener.DOWNLOAD_STOPPED, null);
				}

				stopped = true;

				// JOptionPane.showMessageDialog(Manager.getInstance(),Messages.getString("ActionDownload.PopupDialog.DownloadFinished"));
				// //$NON-NLS-1$

				Manager.getInstance().getEventDispatcher().fireEvent(
						IEventListener.DOWNLOAD_FINISHED, new Object[] {});

				Manager.getInstance().repaint();
			}
                        
                        if(noperm == false)
                            manager.setStatus("No downloads in progress. Press 'Download' button to start."); //$NON-NLS-1$
                        
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}

			// Manager.getInstance().getEventDispatcher().fireEvent(IEventListener.APPLET_WAITING,null);
		}
	}

	/**
	 * Recursively download all selected items
	 * 
	 * @param t_root
	 *            tree root node
	 * @param targetpath
	 *            the destination folder
	 */
	private void recursiveDownload(DownloadTreeNode t_root, File targetpath) {
		for (int i = 0; i < t_root.getChildCount(); i++) {

			Manager.getInstance().getEventDispatcher().fireEvent(
					IEventListener.DOWNLOAD_PROGRESS,
					new Object[] { new Integer(i),
							new Integer(t_root.getChildCount()) });

			// if the download has not been stopped
			if (!stopped) {
                                noperm = false;
                            
				// Get the element at the position i in the Tree
				DownloadTreeNode element = (DownloadTreeNode) t_root
						.getChildAt(i);
				// Debug.println("element=" + element);
				// if it has childs, walk them
				if (element.getChildCount() > 0) {
					if (!stopped)
						recursiveDownload(element, targetpath);
				}
				// if it is a downloadable file, resume it.
				// get the user Object
				DownloadItem di = (DownloadItem) element.getUserObject();
				// scroll to this row automagically
				manager.getExplorer().scrollTo(element);
				if (di.isDownloadable() && di.isQueued()) {

					Manager.getInstance().getEventDispatcher().fireEvent(
							IEventListener.DOWNLOADITEM_STARTING,
							new Object[] { di ,di.getId()});

					// set the progress bar width to 100%
					di.getCellComponent().getProgressBar().setMaximum(100);
					// Concatenate the target filename with complete file path
					String filenameonly = di.getFilename();
					try {
						//log
						//		.info(Messages
						//				.getString("ActionDownload.DebugMessage.FilenameBeforeDecode") + filenameonly); //$NON-NLS-1$
						// filenameonly = URLDecoder.decode(filenameonly,
						// Messages
						// .getString("ActionDownload.FilenameEncoding"));
						// //$NON-NLS-1$
						filenameonly = URLDecoder.decode(filenameonly,"UTF-8"); //$NON-NLS-1$
						//log
						//		.info(Messages
						//				.getString("ActionDownload.DebugMessage.FilenameAfterDecode") + filenameonly); //$NON-NLS-1$
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					// Make the name of the path
					// Create the directories
					String strOutputpath = di.getPaths();
					File outputpath = new File(targetpath, strOutputpath);
					outputpath.mkdirs();
					File outputfile = new File(outputpath, filenameonly);
					//log
					//		.fine(Messages
					//				.getString("ActionDownload.DebugMessage.Downloading") + di.getURL() + " to " //$NON-NLS-1$ //$NON-NLS-2$
					//				+ outputfile);
					di.setOutputfile(outputfile);
					
					if (di.doForceOverwrite() && outputfile.exists()) {
						outputfile.delete();
					}
					
					// make connection to the URL
					HttpURLConnection uc = null;
					InputStream bis;
					FileOutputStream fos;

					// create all subdirectories
					byte[] buf = new byte[8192];
					try {
						// connect and get the size
						URL url = di.getURL();
						if (url == null)
							continue;
						URLConnection urlConnection = url.openConnection();
						if (!(urlConnection instanceof HttpURLConnection)) {
						//	log
						//			.info(Messages
						//					.getString("ActionDownload.ErrorMessage.CannotMakeHTTPConnection") //$NON-NLS-1$
						//					+ urlConnection);
							continue;
						}
						uc = (HttpURLConnection) urlConnection;
						//uc.connect();
						long totalDownloadSize = urlConnection.getContentLength();
						//uc.disconnect();
						// get the size we have already downloaded
						long readBytes = outputfile.length();
						long readFragmentBytes = 0;
						// start timer
						long startTimer = System.currentTimeMillis();
						// check the response
						int responseCode = 200;
						try {
							responseCode = uc.getResponseCode();
						} catch (IOException e2) {
                                                        noperm = true;
                                                        manager.setStatus("Can not download this file, no download permission or file is banned!");
                                                        break;
                                                }
						if (responseCode == 200 || responseCode == 206) {
							// check if we do need to download something
							if (readBytes <= totalDownloadSize) {
								// Reconnect now and try to resume
								// uc = di.getURL().openConnection();
								// uc.connect();
								//uc = (HttpURLConnection) di.getURL()
								//		.openConnection();
								//uc.setRequestProperty("Range", "bytes=" //$NON-NLS-1$ //$NON-NLS-2$
								//		+ outputfile.length() + "-" //$NON-NLS-1$
								//		+ totalDownloadSize);
								//uc.connect();
								// create the download channel between the
								// applet
								// and
								// the webserver
								bis = urlConnection.getInputStream();
								if (outputfile.exists()) {
									fos = new FileOutputStream(outputfile.toString(), true); // true
								} else {
									fos = new FileOutputStream(outputfile);
								}
								// is
								// for
								// append
								int percent;
								int narrowRead;
								// the download fragments loop
								manager.setStatus("Downloading: " + di); //$NON-NLS-1$
								// set a red border around the cell
								di.getCellComponent().setBorder(dottedBorder);
								// jump the tree to the current position
								long stopTimer = startTimer;
								long diffTimer;
								double bandWidth = 0;
								di.getCellComponent().getProgressBar()
										.setStringPainted(true);
								while ((narrowRead = bis.read(buf)) != -1
										&& !stopped) {
										readBytes += narrowRead;
										readFragmentBytes += narrowRead;
										// only write what we got
										fos.write(buf, 0, narrowRead);
										percent = calcPercent(readBytes,
												totalDownloadSize);
										di.getCellComponent().getProgressBar()
												.setValue(percent);
										// calculate the bandwidth
										stopTimer = System.currentTimeMillis();
										diffTimer = (stopTimer - startTimer) / 1000; // seconds
										if (diffTimer > 0) {
											bandWidth = (double) readFragmentBytes
													/ (double) diffTimer;
											bandWidth = bandWidth / 1024;
											// make kilobyte/second
											if (bandWidth > 0) {
												di
														.getCellComponent()
														.getProgressBar()
														.setString(
																(int) bandWidth
																		+ " kb/s"); //$NON-NLS-1$
											}
										}
										treemodel.nodeChanged(element);

										// How to run async?
										Manager
												.getInstance()
												.getEventDispatcher()
												.fireEvent(
														IEventListener.DOWNLOADITEM_PROGRESS,
														new Object[] {
																di,
																new Integer(
																		percent),
																new Double(
																		bandWidth),
																new Long(
																		startTimer),
																new Long(
																		stopTimer),di.getId() });


								}

								// Close the file
								fos.close();
                                                                bis.close();
							}
							// the other case ..
							if (readBytes == totalDownloadSize) {
								// we don't need to download anything
								// because
								// we have already downloaded the whole
								// file
								manager.setStatus("File already complete. Continuing..."); //$NON-NLS-1$
								di.getCellComponent().getProgressBar()
										.setValue(100);
								treemodel.nodeChanged(element);
								handleDownloadComplete(element, di);
							}
							di.getCellComponent().getProgressBar()
									.setStringPainted(false);
							// Close the connection to the webserver
							//uc.disconnect();
							di.getCellComponent().setBorder(noBorder);
							// the other case ..
							if (readBytes > totalDownloadSize) {
								// here we have got a problem!!
								//System.err.println(Messages.getString("ActionDownload.ErrorMessage.DownloadedMoreThanFilesize")); //$NON-NLS-1$
							}
						} // 200ok
						if (responseCode == 404) {
                                                        noperm = true;
                                                        manager.setStatus("Can not download this file, no download permission or file is banned!");
                                                        break;
                                                        
                                                        /*
							System.err.println(Messages.getString("ActionDownload.ErrorMessage.FileDoesNotExist")); //$NON-NLS-1$
							throw new RuntimeException("Error! Nothing to download: "+url.toString());
                                                        */
						}
					} catch (IOException e1) {
                                                noperm = true;
                                                manager.setStatus("Can not download this file, no download permission or file is banned!");
                                                
                                                /*
                                                stopped = true;
                                                handleDownloadComplete(element, di);
                                                
						di.getCellComponent().setBorder(noBorder);
						e1.printStackTrace();
						// Check the error code
						if (uc != null) {
							try {
								System.err
										.println(Messages
												.getString("ActionDownload.ErrorMessage.RequestMethod") //$NON-NLS-1$
												+ uc.getRequestMethod());
								System.err
										.println(Messages
												.getString("ActionDownload.ErrorMessage.ResponseMessage") //$NON-NLS-1$
												+ uc.getResponseMessage());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
                                                */
					}
				}
                                else
                                {
                                    if (di.isDownloadable() == false)
                                    {
                                        String strOutputpath = di.getPaths() + di.getFilename();
                                        if(!strOutputpath.equals("Downloads") && !strOutputpath.equals("\\Downloads") && !strOutputpath.equals("/Downloads") )
                                        {
                                            File outputpath = new File(targetpath, strOutputpath);
                                            outputpath.mkdirs();
                                        }
                                    }
                                    
                                }
			}
			// if not stopped
		}
		return;
	}

	/**
	 * Performed when the download of a single download item is complete.
	 * 
	 * @param element
	 *            tree node element
	 * @param di
	 *            download item instance
	 */
	private void handleDownloadComplete(DownloadTreeNode element,
			DownloadItem di) {

		if (di.doUncompressAfterDownload()) {
			doUncompress(element, di);
			if (di.doDeleteAfterUncompress()) {
				di.getOutputfile().delete();
			}
		}

		if (stopped) {
			Manager.getInstance().getEventDispatcher().fireEvent(
					IEventListener.DOWNLOADITEM_STOPPED,
					new Object[] { di.getOutputfile().toString(), di.getId() });
		}

		Manager.getInstance().getEventDispatcher().fireEvent(
				IEventListener.DOWNLOADITEM_FINISHED,
				new Object[] { di.getOutputfile().toString(), di.getId() });

	}

	/**
	 * @param element
	 * @param di
	 */
	private void doUncompress(DownloadTreeNode element, DownloadItem di) {
		String targetPath = di.getOutputfile().getParent();
		Manager.getInstance().getEventDispatcher().fireEvent(
				IEventListener.DOWNLOADITEM_UNCOMPRESS_STARTING,
				new Object[] { di.getOutputfile().toString(), targetPath, di.getId()});
		try {
			ZipFile zipFile = new ZipFile(di.getOutputfile());
			Enumeration entries = zipFile.entries();
			di.getCellComponent().getProgressBar().setStringPainted(true);
			di.getCellComponent().getProgressBar().setString("Uncompressing"); //$NON-NLS-1$
			di.getCellComponent().getProgressBar().setValue(0);
			long current = 0;
			long total = 0;
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				total += entry.getSize();
			}
			entries = zipFile.entries();
			while (!stopped && entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					File dirFile = new File(targetPath, entry.getName());
					if (dirFile.exists()) {
						//System.err
						//		.println(Messages
						//				.getString("ActionDownload.ErrorMessage.FolderAlreadyExists") //$NON-NLS-1$
						//				+ dirFile);
						continue;
					}
					if (!dirFile.mkdir()) {
						//System.err
						//		.println(Messages
						//				.getString("ActionDownload.ErrorMessage.ErrorMakingFolder") + dirFile); //$NON-NLS-1$
					}
					continue;
				}
				File outFile = new File(targetPath, entry.getName());
				if (outFile.exists()) {
					//System.err
					//		.println(Messages
					//				.getString("ActionDownload.ErrorMessage.OverwritingFile") + outFile); //$NON-NLS-1$
				}
				InputStream is = zipFile.getInputStream(entry);
				OutputStream os = new FileOutputStream(outFile);
				byte[] buf = new byte[65536];
				int c;
				int perc = 0;
				while (!stopped && (c = is.read(buf, 0, buf.length)) > 0) {
					os.write(buf, 0, c);
					current += c;
					perc = (int) (((double) current / (double) total) * 100);
					di.getCellComponent().getProgressBar().setValue(perc);

				}

				Manager.getInstance().getEventDispatcher().fireEvent(
						IEventListener.DOWNLOADITEM_UNCOMPRESS_PROGRESS,
						new Object[] { entry.getName(), outFile.toString(),
								new Integer(perc), new Long(current),
								new Long(total), di.getId() });

				treemodel.nodeChanged(element);
				os.flush();
				os.close();
				is.close();
				if (outFile.length() != entry.getSize()) {
                                    /*
					System.err
							.println(Messages
									.getString("ActionDownload.ErrorMessage.UncompressingFilesizeDoNotMatch") //$NON-NLS-1$
									+ outFile
									+ " (" //$NON-NLS-1$
									+ outFile.length()
									+ "/" + entry.getSize() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                                     */
					continue;
				}
				// Debug.println("Uncompressed " + outFile);
			}
			di.getCellComponent().getProgressBar().setString("Uncompressed."); //$NON-NLS-1$
			di.getCellComponent().getProgressBar().setValue(100);
			di.getCellComponent().getProgressBar().setStringPainted(false);
			treemodel.nodeChanged(element);
			zipFile.close();
		} catch (ZipException e) {
			//System.err.println(Messages.getString("ActionDownload.ErrorMessage.Uncompressing") + di.getOutputfile()); //$NON-NLS-1$
			e.printStackTrace();
		} catch (IOException e) {
			//System.err.println(Messages.getString("ActionDownload.ErrorMessage.AccessingFile") + di.getOutputfile()); //$NON-NLS-1$
			e.printStackTrace();
		}
		if (stopped) {
			Manager.getInstance().getEventDispatcher().fireEvent(
					IEventListener.DOWNLOADITEM_UNCOMPRESS_STOPPED,
					new Object[] { di.getOutputfile().toString(),di.getId() });
		}
		Manager.getInstance().getEventDispatcher().fireEvent(
				IEventListener.DOWNLOADITEM_UNCOMPRESS_FINISHED,
				new Object[] { di.getOutputfile().toString(),di.getId() });
	}

	/**
	 * @return an integer percentage value
	 */
	private int calcPercent(long readBytes, long totalDownloadSize) {
		long result = (readBytes * 100) / totalDownloadSize;
		return (int) result;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JButton clickedbutton = (JButton) e.getSource();
		if (clickedbutton.getActionCommand().equalsIgnoreCase("DOWNLOAD")) { //$NON-NLS-1$
			clickedbutton.setEnabled(false);
			manager.getControlPanel().getButtonStop().setEnabled(true);
			stopped = false;
			// startbutton.setActionCommand("STOP");
			// startbutton.setText("Stop");
		}
		if (clickedbutton.getActionCommand().equalsIgnoreCase("STOP")) { //$NON-NLS-1$
			clickedbutton.setEnabled(false);
			manager.getControlPanel().getButtonDownload().setEnabled(true);
			stopped = true;
			// startbutton.setActionCommand("DOWNLOAD");
			// startbutton.setText("Download");

		}
	}
}

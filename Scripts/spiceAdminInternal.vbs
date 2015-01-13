set objFSO=CreateObject("Scripting.FileSystemObject")
Set objShell = CreateObject("WScript.Shell")
sUserProfile=CreateObject("WScript.Shell").ExpandEnvironmentStrings("%UserProfile%\")
sUserDesktop = objShell.SpecialFolders("Desktop")

'Modify Variables in this section
IconSourceFile= "\\cobalt\data\d\Custom Software\Global_Shortcuts\icon.ico" 'Source file for display icon
IconFolder=sUserProfile & "\Icons\" 'Folder that the IconSourceFile will be copied to (c:\documents and settings\%username%\ICONS)
IconFile= "ICON.ico" 'Icon File Name (to be saved on local computer)
ShortcutName= "\Helpdesk Admin.lnk" 'Name of the Shortcut to appear on Desktop
URL= "http://mfalcon:9675/tickets/list/open_tickets" 'URL/Webaddress

IF NOT objFSO.FileExists (IconFolder & IconFile) Then
IF NOT objFSO.FolderExists (IconFolder) Then
objFSO.CreateFolder (IconFolder)
End if
objFSO.CopyFile IconSourceFile, IconFolder, TRUE
End If
Set objShortcutUrl = objShell.CreateShortcut(sUserDesktop & ShortcutName)
objShortcutUrl.TargetPath = URL
objShortcutUrl.IconLocation = IconFolder & IconFile
objShortcutUrl.Save
wscript.quit() 


copy "\\cobalt\data\d\g\Custom Software\New Computer Programs\cobalt app\MSCAL.ocx" "%systemroot%\System32"
copy "\\cobalt\data\d\g\Custom Software\New Computer Programs\cobalt app\MSCAL.ocx" "%systemroot%\SysWow64"


Regsvr32 /s %systemroot%\SysWow64\mscal.OCX
Regsvr32 /s %systemroot%\System32\mscal.ocx

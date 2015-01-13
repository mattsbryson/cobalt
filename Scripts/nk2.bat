if exist C:\Users goto seven
if not exist C:\users goto xp


:seven
copy /y "C:\Users\%username%\AppData\Local\Microsoft\Outlook\Outlook.NK2" "H:\"
goto end

:xp
copy /y "C:\Documents and Settings\%username%\Application Data\Microsoft\Outlook\Outlook.nk2" "H:\"
goto end

:end

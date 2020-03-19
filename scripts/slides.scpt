#!/usr/bin/osascript

set destPath to ((path to home folder) as text) & "Projects:conf-2020-scalaua:Prototyping-The-Future-ScalaUA-2020.pdf"

tell application "Keynote"
  save document 1
  export document 1 as PDF to file (destPath)
end tell
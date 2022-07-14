# sPlots
Guten Tag,
hier ist mein Plot-Plugin.

#Permissions:
Admin-Perm (Überall abbauen, andere Plots verstellen): splots.admin
Flag-Perms: splots.flag.<flag>
Befehle: splots.<befehl>

#Befehle:
/plot <Befehl>

home - Teleportiere dich zu deinem Plot
auto - Ein zufälliges Plot erhalten - splots.auto
flag - Dinge auf einem Plot verstellen - splots.flag
tp - Dich zu der angegebenen Plot-ID teleportieren - splots.tp
trust / untrust - Vertraue oder entferne Leute von deinem Plot - splots.trust / splots.untrust
setowner - Verändere den Besitzer eines Plots - splots.setowner
claim - Bekomme das Plot auf dem du stehst - splots.claim
middle - Teleportiere dich in die Mitte von deinem Plot - splots.middle
clear - Leere dein Plot - splots.clear
delete - Lösche dein Plot - splots.delete
info - zeige Infos von deinem Plot an - splots.info
rand - Verändere den Rand von deinem Plot - splots.rand
editrand - Verändere die Ränder bei rand - splots.editrand
wall - Verändere die Wand von deinem Plot - splots.wand
editwall - Verändere die Wände bei wall - splots.editwall
  
  
  #API - Noch nicht fertig
  Ihr könnt die Jar schon in euer Maven-Projekt reinshaden und die Events nutzen:
  
  '''
  java
  @EventHandler
  public void onPlayerPlotEntry(PlayerEntryPlotEvent event) {
  
  }
  
  '''

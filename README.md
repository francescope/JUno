# JUno
Gioco "Uno" sviluppato in Java utilizzando il pattern Model-View-Controller (MVC) con l'integrazione del pattern Observer-Observable. L'obiettivo è ottenere un'architettura scalabile e ben strutturata, separando chiaramente logica, interfaccia e gestione degli eventi.

LINK JAR: https://drive.google.com/drive/folders/1gQnD0u1lkplLF6-ETSl95_fTVGP4zrpq?usp=sharing

Pattern MVC con l'uso di Observer-Observable
Il pattern Model-View-Controller (MVC) viene adattato usando il pattern Observer-Observable per migliorare la separazione dei ruoli e ridurre le dipendenze dirette tra il Model e la View. Questo approccio è una scelta valida e scalabile per progetti in cui il modello deve notificare cambiamenti alle viste senza conoscerne i dettagli.


Model (Logica del Gioco - Observable)

Il Model rappresenta il cuore logico dell'applicazione. Gestisce lo stato del sistema e la logica di business senza preoccuparsi di come queste informazioni verranno mostrate o utilizzate.
Ruolo nel pattern Observer-Observable:
Il Model estende Observable e quindi diventa una fonte di eventi. Quando lo stato cambia (es. avanzamento del gioco, modifica dello stato di un giocatore), il modello chiama notifyObservers().
Questo meccanismo consente di notificare tutte le View registrate senza che il Model debba avere una dipendenza diretta dalle istanze delle viste.
Esempio pratico nel progetto:
Classi come Game, GameClassic, Player, e PlayerAI si occupano di gestire gli elementi fondamentali della logica del gioco (es. turni, punteggi, decisioni AI).
Quando, ad esempio, il punteggio di un giocatore cambia, il Model chiama setChanged() seguito da notifyObservers().


View (Interfaccia Grafica - Observer)

La View è responsabile della presentazione grafica e dell'interazione con l'utente. Si occupa di visualizzare lo stato corrente del gioco (es. griglia, punteggi, messaggi) e fornire i controlli necessari per giocare (es. bottoni, input).
Ruolo nel pattern Observer-Observable:
La View implementa Observer e si registra al Model come osservatore usando addObserver(this).
Quando il Model cambia, la View viene notificata tramite il metodo update(Observable o, Object arg) e può richiedere lo stato aggiornato del modello per riflettere le modifiche nell'interfaccia grafica.
Questo approccio evita che il Model abbia responsabilità di aggiornare direttamente la grafica, rispettando così il principio di Single Responsibility.
Struttura delle viste nel progetto:
La View è composta da diversi pannelli grafici (JPanel) che organizzano l'interfaccia utente (es. tabella di gioco, barra dei punteggi, messaggi di stato). Ogni pannello è progettato per rispondere ai cambiamenti nel Model.


Controller (Gestore degli Input)
   
Il Controller agisce come intermediario tra la View e il Model. Traduce le azioni dell'utente (es. click, input) in operazioni sul Model.
Ruolo nel pattern MVC:
Riceve eventi generati dalla View (es. pressione di un bottone) e li interpreta per aggiornare il Model.
Esempio nel progetto:
Un controller potrebbe ascoltare un evento generato da un pulsante nella GUI (es. "inizia nuova partita") e chiamare metodi appropriati del Model per avviare una nuova istanza di gioco.


Esempio di flusso
L'utente effettua un'azione tramite la GUI (es. clicca su un bottone per effettuare una mossa).
La View passa l'evento al Controller.
Il Controller traduce l'evento e chiama un metodo del Model (es. Game.makeMove()).
Il Model aggiorna il suo stato interno e chiama notifyObservers().
La View, registrata come osservatore, riceve la notifica e aggiorna la GUI richiedendo al Model il nuovo stato.

Swing
Per realizzare l'interfaccia grafica del progetto, è stata utilizzata la libreria Swing, una componente della piattaforma standard di Java progettata per la creazione di GUI flessibili e interattive. Swing offre un'ampia gamma di componenti che sono stati integrati per costruire un'interfaccia utente modulare, intuitiva e facilmente scalabile.

import sys
import time
import platform
import select
from pypresence import Presence

ID = '1131227035616956497'
RPC = Presence(client_id=ID)

RPC.connect()

def set_activity(details='RPC DETAILS'):
    if RPC is None:
        return

    RPC.update(
        details=details,
        state='RPC STATE',
        large_image='TTTCom',
        small_image='TTTCom',
        small_text='SMALL IMAGE TEXT',
        start=int(time.time()),
        buttons=[
            {'label': 'Youtube', 'url': 'https://github.com/Lojaw/TTTMod'},
            {'label': 'Discord', 'url': 'https://discord.gg/x2CxX5hhtM'}
        ]
    )

last_signal_received = time.time()
while True:
    set_activity()
    time.sleep(10)  # Aktualisierung alle 10 Sekunden
    if platform.system() == "Windows":
        if not sys.stdin.isatty():  # Überprüfen, ob Daten auf der Standard-Eingabe verfügbar sind
            signal = sys.stdin.readline().strip()  # Signal lesen
            if signal == 'alive':
                last_signal_received = time.time()
    elif platform.system() == "Linux":
        while select.select([sys.stdin,],[],[],0.0)[0]:
            signal = sys.stdin.readline().strip()  # Signal lesen
            if signal == 'alive':
                last_signal_received = time.time()
    if time.time() - last_signal_received > 10:  # Wenn seit mehr als 10 Sekunden kein Signal empfangen wurde, beenden
        break
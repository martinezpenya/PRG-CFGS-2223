# Aplicacions amb BD orientades a objectes

POO i gestors de base de dades
Si bé les BD relacionals són les més populars i les que tenen més acceptació,
la seva utilització dins una aplicació orientada a objectes implica un procés de
traducció del diagrama UML original a un model relacional, totalment basat
en taules. En aquesta traducció es perden moltes de les funcionalitats bàsiques
de l’orientació a objectes, que s’han de simular d’alguna manera: referències a
objectes, classes associatives, llistes d’objectes, herència, etc. Quan el diagrama
és de certa complexitat, la traducció pot esdevenir molt complicada.
Per resoldre aquest problema hi ha les BD orientades a objectes (BDOO). Aques-
tes, en lloc d’organitzar les dades en taules, les organitzen exactament tal com ho
fa un diagrama UML, mitjançant la definició del conjunt de classes i relacions
entre elles. Per tant, no cal fer cap traducció.
Per evitar confusions, fem servir el terme BDR per referir-nos explícitament
a una BD relacional i el terme BDOO per referir-nos a una BD orientada a
objectes, de manera que ambdós quedin diferenciats.
Actualment, l’aplicació de BDOO es limita a àmbits molt concrets, especialment
els vinculats a àrees científiques. La seva implantació en aplicacions comercials
d’àmbit general és molt baixa. Un dels problemes principals de les BDOO és
que els fabricants tendeixen a crear solucions incompatibles, que no obeeixen cap
especificació concreta. Al contrari que en el cas de les BDR, és molt possible que
una aplicació client feta pel producte d’un fabricant concret no funcioni sobre una
BDOO d’un altre fabricant. De fet, a les BDOO que suporten Java no s’accedeix
mitjançant JDBC, ja que aquest mecanisme és específic per a BDR, sinó que
normalment s’hi accedeix usant biblioteques específiques per a cada fabricant.
El Java té una especificació
per a BDOO anomenada
JDO (Java data objects).
3.1 Els llenguatges ODL i OQL
De la mateixa manera que hi ha l’SQL com a llenguatge estàndard per accedir
a una BDR independentment del fabricant, hi ha un llenguatge per accedir a
les dades d’una BDOO: el llenguatge de consultes a objectes (object query
language, OQL). Addicionalment, hi ha el llenguatge de descripció d’objectes
(object description language, ODL), que serveix per especificar el format d’una
BDOO: quina mena d’objectes pot contenir i les seves relacions. Malauradament,
si bé aquests llenguatges estan especificats, i com ja s’ha dit, no es pot comptar
amb el fet que qualsevol fabricant realment els suporti. De cap manera arriben al
grau d’acceptació de l’SQL.
Programació orientada a objectes
 74
 POO i gestors de base de dades
Si retornem a un model
orientat a objectes, amb
referències, no cal l’atribut
“id”.
3.1.1 El llenguatge ODL
El llenguatge ODL s’utilitza per definir classes d’objectes persistents dins una
BDOO, de manera que els seus objectes es puguin emmagatzemar. Dins la
declaració de cada classe s’inclou:
• El nom de la classe.
• Declaracions opcionals de claus primàries.
• La declaració de l’extensió: el nom del conjunt d’instàncies existents.
• Declaracions d’elements: atributs, relacions o mètodes.
La sintaxi és la següent (entre claudàtors s’indiquen camps opcionals):
1
2
3
4
5
6
7
8
class nomClasse [(key nomAtribut)] {
attribute tipusAtribut nomAtribut;
...
relationship tipus<nomClasseDestinació> nomRelacio;
...
tipusRetorn nomMetode(params) [raises (tipusExcepcio)]
...
}
Com es pot apreciar, simplement és un canvi de sintaxi respecte al llenguatge Java
pròpiament, però la majoria d’elements d’una classe són clarament identificables.
L’única diferència és la declaració explícita de les relacions en forma de la paraula
clau relationship, en contrast en Java, que es tradueixen a atributs. Hi ha diferents
tipus de relacions segons la cardinalitat que es vol expressar. De fet cadascun
d’aquests tipus té una certa correspondència amb les classes que s’usen en Java
per implementar relacions. Per a cardinalitat 1, és suficient de posar el nom de la
classe destinació. En cas de cardinalitat múltiple, es pot triar entre diferents tipus:
• <nomClasseDestinació>, si la relació és només a un únic objecte.
• Set<nomClasseDestinació>, un conjunt no ordenat sense repeticions.
• Bag<nomClasseDestinació>, un conjunt no ordenat amb repeticions.
• List<nomClasseDestinació>, un conjunt ordenat amb repeticions, amb
insercions eficients.
• Array<nomClasseDestinació>, un conjunt ordenat amb repeticions.
El seu significat és el mateix que el de les classes homònimes del Java (capacitat
d’haver-hi repeticions d’elements, ordenades o no per índex, etc.). Normalment,
la més usada és Set<nomClasseDestinació>.
Tot seguit es mostra com es podria representar dues classes interdependents
anomenades Client i Encarrec, que emmagatzemen dades a una aplicació de
gestió de clients, mitjançant ODL.
Programació orientada a objectes
 75
1
 class Client (key id) {
2
 attribute int id;
3
 attribute String nom;
4
 attribute String adreçaPostal;
5
 attribute String adreçaMail;
6
 attribute String telefon;
7
8
 relationship Set<Encarrec> encarrecs;
9
10
 String getId();
11
 ...
12
 }
13
14
 class Encarrec (key id)
 {
15
 attribute int id;
16
 attribute Date data;
17
 ...
18
 }
L’herència entre classes s’inicia en la seva declaració mitjançant la paraula clau
extends seguit del nom de la superclasse:
1
2
3
class nomClasse extends nomSuperClasse {
...
}
POO i gestors de base de dades
3.1.2 El llenguatge OQL
El llenguatge OQL es limita a permetre consultes sobre una BDOO. El seu
operador principal és SELECT, el qual té una gran similitud amb l’equivalent SQL.
Tot i així, té algunes particularitats degudes a la manera com s’estructuren les
dades mitjançant l’orientació a objectes (per exemple, no hi ha taules, és clar).
La sintaxi general és:
1
2
3
SELECT valor1,valor2,...
FROM llista de col.lecions i noms per membres típics
WHERE condició
Atès que ara ja no hi ha taules, cal tenir en compte dues coses. D’una banda,
la llista de col·leccions especificada en l’apartat FROM correspon a alguna de les
classes declarades. Juntament al nom d’aquesta classe s’especifica la variable que
s’usarà en els termes SELECT i WHERE per referenciar valors. D’altra banda, els
valors que es volen consultar o comparar són atributs de classes, pel que la manera
de referir-s’hi és mitjançant la nomenclatura: nomClasse.nomAtribut. Aquesta
possibilitat també es pot usar per indicar-ne les relacions.
Per exemple, a una aplicació de gestió de clients, si es volen consultar els clients
de la BDOO d’acord amb la definició de les seves classes, es pot fer:
1
2
3
SELECT c.adreçaPostal, c.telefon
FROM Clients c
WHERE c.nom = "Client1"
Programació orientada a objectes
76
 POO i gestors de base de dades
Aquesta consulta retorna l’adreça postal i el telèfon del client amb nom “Client1”.
També és possible accedir als encàrrecs per mitjà dels clients, seguint la seva
relació:
1
2
3
SELECT e.data
FROM Clients c, c.encarrecs e
WHERE c.adreçaMail = "email1@domini.com"
Aquesta consulta retorna la data de tots els encàrrecs del client amb adreça de
correu “email1@domini.com”.
Les classes de db4o estan
dins els packages
com.db4o...
3.2 La llibreria db4O
Tot i els esforços per estandarditzar l’ús de les BDOO, no es pot dir que actualment
hi hagi cap equivalent al llenguatge SQL. Tot i que sobre el paper hi ha l’ODL i
l’OQL, a la pràctica ara mateix no hi ha cap llengua franca que es pugui garantir
que està suportada, almenys en els seus aspectes fonamentals, per totes les bases
de dades, tot i que després cada fabricant pugui afegir igualment les seves pròpies
extensions propietàries. Per tant, cada tipus de BDOO ofereix el seu propi sistema
per accedir als objectes emmagatzemats. Afortunadament, com aviat veureu, això
no és gaire problemàtic, ja que l’avantatge d’usar una BDOO és poder crear codi
on operar amb objectes persistents; és gairebé igual que treballar amb objectes a
memòria, i, per tant, els mecanismes que ofereixen les diferents BDOO sovint són
molt semblants a treballar amb objectes directament a memòria. El que varia són
les llibreries de classes a usar, però no la idea general.
En aquest apartat es veurà un cas concret d’accés a una BDOO anomenada
db4o, de lliure distribució, actualment amb versions per al Java i per a .NET.
Evidentment, aquesta secció se centra exclusivament en la versió per al Java. No
es troba en la distribució estàndard del Java, i, per tant, s’ha de descarregar i afegir
als vostres projectes a part.
La llista de passos per interactuar amb aquesta BDOO és molt semblant a l’empra-
da mitjançant JDBC, si bé la manera com es fa amb codi Java és completament
diferent en alguns aspectes.
Un cop s’han emmagatzemat objectes a una BDOO db4o, ja no es pot
modificar la classe d’aquests objectes. Si es modifica (i, per tant, es torna
a compilar el fitxer JAVA), totes les dades que hi ha dins deixen de tenir
validesa. Si es vol usar la nova versió de la classe, cal tornar a generar el
contingut de la BDOO des de zero.
Programació orientada a objectes
 77
 POO i gestors de base de dades
3.2.1 Obertura de la BDOO
Una BDOO db4o no és més que un fitxer, si bé força complex en la seva estructura
interna. Per tant, alguns dels aspectes són semblants, com ara l’accés a les dades
emmagatzemades dins, que és semblant a com es faria amb un fitxer qualsevol.
Aquest és el cas de la seva obertura, per tal de poder llegir les seves dades o
escriure’n, i el seu posterior tancament quan ja no cal usar-lo més. El mètode
estàtic openFile de la classe Db4oEmbedded permet que es pugui obrir aquest
fitxer. Com succeeix en treballar amb fitxers, si aquest no existeix, se’n crea un de
nou, amb la BDOO buida.
1
2
3
4
5
import com.db4o.*;
...
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
//Accions amb la BDOO
db.close();
Normalment, els fitxers de
db4o s’escriuen amb
l’extensió ”.db4o”
Mitjançant l’objecte resultant de la crida, una instància d‘ObjectContainer , es
podran dur a terme totes les accions amb la BDOO. Fins a cert punt, és l’equivalent
a una connexió a una BDR mitjançant JDBC. Un cop s’ha acabat de treballar,
sempre cal tancar la BDOO usant el mètode close.
Ara bé, aquesta és una aproximació molt simple a l’ús d’una BD, ja que el fitxer ha
d’estar emmagatzemat en local a la mateixa màquina que executa l’aplicació. Per
poder tractar les dades des d’una altra màquina, caldrà copiar tant l’aplicació com
també el fitxer de la BD. Normalment, el model de treball amb una BD es basa
que hi ha un servidor, on s’executa la BD, i el desenvolupador genera el client, que
s’hi connecta per xarxa. Un cop establerta la connexió, pot enviar peticions a la
BD, i un cop dutes a terme totes les tasques, la tanca.
Les llibreries de db4o no proporcionen cap servidor en forma de programa que
només cal instal·lar i configurar en un equip. Afortunadament, aquesta tasca és
molt simple, ja que es pot fer en poques línies de codi. Dins aquest model d’accés
a les dades, la BD continua essent un únic fitxer, en aquest cas emmagatzemat al
servidor central, i les llibreries ja proporcionen tots els mecanismes necessaris
per publicar el servei a la màquina i poder accedir-hi remotament de manera
transparent, com si en realitat fos un fitxer en local en l’equip que executa
l’aplicació client.
Per posar en marxa un servidor, cal usar el mètode estàtic openServer de la classe
Db4oClientServer (al package com.db4o.cs). Un cop s’executa el mètode,
el servei d’accés a la DBOO es posa en marxa a l’equip local on s’ha executat.
Mentre el programa segueixi en execució, estarà disponible. Aquest mètode
requereix tres paràmetres:
• Un nou objecte de configuració del servidor, que sempre es genera cridant
Db4oClientServer.newServerConfiguration().
Programació orientada a objectes
 78
 POO i gestors de base de dades
• El nom del fitxer on es desa la BDOO. Si no existeix, se’n crearà un de nou
buit.
• El port on s’executarà el servei.
Sovint, en engegar o apagar
un servei db4o apareix un
missatge de depuració per
la consola d’errors del Java.
La creació d’un servidor retorna un objecte ObjectServer, a partir del qual es
poden configurar certs aspectes del comportament del servei. El més important de
tots és poder afegir usuaris i contrasenyes que limitin qui pot accedir a la BDOO
remotament. Això es fa usant el mètode grantAccess.
Vegem un exemple de servidor db4o, molt senzill, però més que suficient per
provar-ne el funcionament i els exemples d’aquest apartat si es desitja. En aquest
cas, s’ha fet que el programa que executa el servei no finalitzi fins que l’usuari pitgi
“Q” o “q”. En fer-ho, el servei s’apaga i deixa d’estar accessible remotament.
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
importimportimportjava.io.Scanner;
com.db4o.*;
com.db4o.cs.Db4oClientServer;
public class Server {
public static void main (String[] args) throws Exception {
ObjectServer sv = Db4oClientServer.openServer(Db4oClientServer.
newServerConfiguration(), "BDRemota.db4o", 7000);
sv.grantAccess("usuari", "contrasenya");
Scanner in = new Scanner (System.in);
System.out.println("Pitja [Q] per tancar el servidor.");
while (in.hasNext()) {
if ("Q".equalsIgnoreCase(in.next())) break;
}
}
}
Des del punt de vista de l’aplicació que vol accedir a la BD remota, cal usar
el mètode openClient de la classe Db4oClientServer. Ara bé, calen alguns
paràmetres per poder identificar on es vol accedir:
• L’identificador de la màquina remota.
• El port del servei, tal com s’ha configurat en fer openServer.
• Un nom d’usuari i una contrasenya vàlids.
Si algun d’aquests paràmetres no és correcte, es llançarà una excepció indicant
que la connexió no s’ha pogut establir.
1
2
3
4
5
6
import com.db4o.*;
import com.db4o.cs.Db4oClientServer;
...
ObjectContainer db = Db4oClientServer.openClient("lamevamaquina.domini.cat",
7000, "usuari","contrasenya");
//Accions amb la BDOO
db.close();
Localhost
Per connectar-se a l’equip local,
pel cas on tant el client com el
servidor s’executen la mateixa
màquina,es pot usar el nom de
host, localhost. Això és útil per
fer proves en un únic equip.
L’objecte retornat en aquest cas també és un ObjectContainer, per la qual cosa,
un cop establerta la connexió amb la DB remota amb aquesta crida, totes les
operacions que es poden dur a terme són exactament iguals que si es fessin accedint
a un fitxer en local.
Programació orientada a objectes
 79
 POO i gestors de base de dades
3.2.2 Emmagatzematge de nous objectes
Per emmagatzemar qualsevol objecte del vostre programa dins la BDOO, només
cal cridar el mètode store que proporciona l‘ObjectContainer, obtingut en
obrir la BD (ja sigui en un fitxer local o remot). Aquest mètode només té
un paràmetre, que és l’objecte a emmagatzemar. Mitjançant aquest mètode es
pot desar qualsevol tipus d’objecte, sense que per aquest fet s’hagi de fer cap
modificació al seu codi font.
Per exemple, suposeu que es volen gestionar els encàrrecs que duen a terme clients
d’una empresa i, per a tal efecte, s’han generat les classes següents, que inicialment
no es van desenvolupar amb el propòsit de ser integrades dins cap BDOO db4o.
Noteu que la classe Client només permet canviar l’adreça electrònica un cop s’ha
instanciat (usant el mètode setAElectronica).
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
import java.util.*;
public class Client {
private String nom;
private String aPostal;
private String aElectronica;
private String telefon;
private List<Encarrec> liComandes = new LinkedList<Encarrec>();
public Client(String n, String ap, String ae, String t) {
nom = n;
aPostal = ap;
aElectronica = ae;
telefon = t;
}
public String getNom() {
return nom;
}
public String getAPostal() {
return aPostal;
}
public String getAElectronica() {
return aElectronica;
}
public void setAElectronica(String ae) {
aElectronica = ae;
}
public String getTelefon() {
return telefon;
}
public int getNreComandes() {
return liComandes.size();
}
public void addComanda(Encarrec e) {
liComandes.add(e);
}
public List<Encarrec> getComandes() {
return liComandes;
}
@Override
public String toString() {
String res = nom + " : " + aPostal + " : (" + aElectronica + ", " + telefon

+ ")\n";
Iterator<Encarrec> it = liComandes.iterator();
while (it.hasNext()) {
Encarrec e = it.next();
res += e.toString() + "\n";
}
return res;
}
Programació orientada a objectes
 80
 POO i gestors de base de dades
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
import java.util.Date;
public class Encarrec {
private String nomProducte;
private int quantitat;
private Date data;
public Encarrec(String n, int q) {
nomProducte = n;
quantitat = q;
data = new Date();
}
public String getNom() {
return nomProducte;
}
public int getQuantitat() {
return quantitat;
}
public Date getData() {
return data;
}
@Override
public String toString() {
return getData()+ " − " + getNom() + " (" + getQuantitat() + ")";
}
}
El codi d’un programa que emmagatzema quatre clients a la BDOO, entre els
quals un d’ells ja té tres encàrrecs associats, seria el que hi ha a continuació,
basat, senzillament, a fer crides successives a store, passant com a paràmetre
cada objecte que es vol emmagatzemar. Per simplificar, se suposa que s’obre una
BDOO ubicada en un fitxer local, però per al cas remot, seria exactament el mateix.
Recordeu que sempre cal controlar les excepcions en accedir a les dades.
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
import com.db4o.*;
public class EmmagatzemaClients {
public static void main(String[] args) throws Exception {
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
try {
Client[] clients = {
new Client("Client1", "Adreça1", "e − mail1@domini.com", "+34931112233"),
new Client("Client2", "Adreça2", "e − mail2@domini.com", "+34932223344"),
new Client("Client3", "Adreça3", "e − mail3@domini.com", "+34931112233"),
new Client("Client4", "Adreça3", "e − mail4@domini.com", "+34931112233")
};
clients[2].addComanda(new Encarrec("Impressora",1));
clients[2].addComanda(new Encarrec("Toner Impressora",4));
clients[2].addComanda(new Encarrec("Paquest A4", 20));
for(int i = 0; i < clients.length; i++) {
db.store(clients[i]);
}
} finally {
db.close();
}
}
}
En aquest codi hi ha un aspecte molt important en què val la pena fixar-se en
detall. Si l’examineu, veureu que, tot i que els objectes que cal emmagatzemar
són tant els clients com els encàrrecs, enlloc es fa cap store per als encàrrecs.
Només es fa per als clients. Això es deu al fet que, en les BDOO, en fer
persistent un objecte, aquesta persistència es propaga a tots els objectes enllaçats
Programació orientada a objectes
 81
també, de manera transitiva, fins que tot el mapa d’objectes, el graf d’enllaços
que parteix de l’objecte emmagatzemat es troba al complet a la BDOO. Això
es fa automàticament sense necessitat que ho faci el programador. Aquest
comportament, esquematitzat a la figura 3.1, també succeeix quan es recuperen
les dades amb alguna cerca, com veureu properament.
Fig u ra 3. 1. Tractament dels mapes d’objectes sobre una BDOO
POO i gestors de base de dades
Una pregunta que pot sorgir és: què passa si, a posteriori, s’emmagatzema un
objecte que, a causa d’aquest comportament, ja existeix a la BDOO? Per exemple,
si es fa un store d’un dels tres encàrrecs, quan aquest de fet ja està a la BDOO, ja
que s’ha desat automàticament en emmagatzemar el client 3. La resposta és que
no passa res. La BDOO ja detecta que es tracta del mateix objecte i, per tant, no
es generen dues còpies.
Això és possible ja que, recordeu que una de les bases de la OO és “Tot és un
objecte, amb una identitat pròpia”. O sigui, tot objecte s’identifica amb una única
referència. Aquesta pot estar replicada en diferents variables, però totes apunten
a un únic objecte a memòria. Això també es compleix dins la BDOO, i, per tant,
aquesta és capaç d’identificar diferents operacions amb un mateix objecte.
Ara bé, aquest comportament té unes altres implicacions que cal tenir també ben
presents. Suposeu que aquest mateix programa l’executeu 3 vegades consecutives.
Quants objectes client hi haurà emmagatzemats a la BDOO després de la darrera
execució? La resposta és que n’hi haurà 12, ja que els objectes de cada execució
són independents entre si. Tot i que el contingut dels objectes en successives
execucions és exactament el mateix, l’objecte en si és diferent, tenen diferents
referències, i, per tant, es considera un nou element a la BDOO. En conseqüència,
cal anar amb molt de compte en emmagatzemar nous objectes entre execucions
diferents del programa, ja que això sempre implicarà la creació d’un nou element
a la BDOO.
Programació orientada a objectes
 82
 POO i gestors de base de dades
3.2.3 Cerca d’objectes
Els mecanismes de lectura d’una BD solen ser els més importants, ja que
normalment són els que s’usen més sovint. La llibreria db4o ofereix dos sistemes
diferents per dur a terme aquest procés, diferenciats únicament per com es
discrimina quins objectes cal retornar de la BDOO. En qualsevol dels dos casos,
el que es retorna és un conjunt d’objectes, exactament tal com els heu definit a
les vostres classes, empaquetat dins un contenidor específic de db4o anomenat
ObjectSet<T>. Aquesta és una classe genèrica, de manera que, en declarar-ne
una variable, cal establir sempre el tipus d’objectes que contindrà (com passa amb
altres contenidors del Java:List, Set...). Per exemple, si es volen consultar clients,
caldrà usar la definició ObjectSet<Client>.
Una de les característiques més interessants de les cerques dins una BDOO és la
recuperació d’objectes enllaçats entre si, de manera que si, en recuperar un objecte,
aquest contenia a la vegada referències a altres objectes, aquests objectes també
són recuperats.
I així fins a un cert nivell de profunditat, que per defecte val 5, però que es pot
modificar utilitzant el codi:
1
2
EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
conf.common().activationDepth(novaProfunditat);
L’exemple més senzill d’aquest comportament són els atributs de tipus String,
que també són pròpiament objectes, i són restaurats junt amb l’objecte original.
Però aquest comportament també es compleix per a objectes de qualsevol altra
classe, ja sigui del Java o creada per vosaltres.
Cerques per exemple
Les cerques per exemple (Query-By-Example) són les més senzilles. Es basen a
crear una instància del tipus d’objecte a cercar, i només assignar valors als atributs
sobre els quals es vol cercar una coincidència exacta. La resta, es posen a null
(en el cas de valors numèrics, a 0). Llavors, s’invoca el mètode queryByExample,
usant com a paràmetre aquesta instància.
Per exemple, per cercar tots els clients que tenen com a adreça la cadena de text
“Adreça3” es faria d’acord al codi que hi ha a continuació. En executar-se el codi,
atesos els quatre clients d’exemple existents a la BDOO, dins l‘ObjectSet hi
haurà 2 objectes Client: el que té com a Client3 i el que té Client4.
1
2
3
4
5
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
...
Client ex = new Client(null, "Adreça3", null, null);
ObjectSet<Client> result = db.queryByExample(ex);
...
Programació orientada a objectes
 83
L‘ObjectSet pot ser recorregut seqüencialment mitjançant els mètodes
hasNext(), que indica si encara hi ha elements per recórrer, i next(), que llegeix
un element i avança una posició. En aquest sentit, es comporta exactament igual
que un Iterator de les llibreries estàndard del Java. Per exemple, per tal de
mostrar per pantalla tots els elements obtinguts per la consulta, es podria fer el
següent:
1
2
3
4
5
ObjectSet<Client> result = ...
while (result.hasNext()) {
Client cli = result.next();
System.out.println(cli);
}
POO i gestors de base de dades
Atès que ObjectSet és una classe genèrica, el mètode next() retorna directament
una instància del tipus indicat, i no cal fer cap “cast”. Ara bé, cal anar amb compte
a definir sempre el tipus dels elements de l‘ObjectSet, en declarar-ne la variable,
de la mateixa classe que la instància usada en invocar queryByExample. En cas
contrari, es produirà un error per manca de concordança de tipus.
Si bé aquesta mena de cerques són molt senzilles de fer, no permeten res més
que la comparació directa d’atributs. Tanmateix, tampoc no permeten fer cerques
sobre valors que siguin null o 0, ja que són les condicions per ignorar-los com a
criteri de cerca. També tenen la restricció que no poden usar-se a partir d’objectes
que no permeten inicialitzar atributs a valors null o 0.
Cerques natives
Normalment, les cerques que es voldran fer dins una BD van més enllà de les
simples concordances directes entre valors d’atributs, i es desitjarà poder avaluar
tota mena de condicions, a gust del desenvolupador, tal com permet SQL (o més).
Hi ha dos mecanismes per dur a terme cerques d’acord a criteris complexos dins
db4o, però la més potent, i, a la vegada, la més senzilla, són les cerques natives
(Native Queries). Es basen en l’execució d’un codi Java per avaluar si un objecte
dins la BDOO compleix la condició de cerca o no. En basar-se només en codi
Java, la seva versatilitat és la mateixa que en qualsevol programa possible, o sigui,
molt gran.
Per executar una cerca nativa s’usa el mètode query, que necessita com a
paràmetre una implementació de la classe Predicate<T> (pertanyent al package
com.db4o.query).
Predicate<T> és una classe genèrica abstracta, per la qual cal indicar quina mena
d’objectes és capaç de processar en emprar-la. L’únic mètode abstracte que cal
implementar és match, que s’ha de fer que s’avaluï a true si es considera que
l’objecte passat com a paràmetre compleix el criteri de cerca, o false en cas
contrari. Aquest mètode s’executarà passant com a paràmetre tots els objectes
de la BDOO que es corresponguin al tipus escollit, un per un. Ara bé, el codi
d’aquest mètode l’heu d’implementar vosaltres, ja que és el que ha de prendre la
decisió de si un objecte compleix o no el criteri de cerca, i, per tant, el podeu fer
al vostre gust. Per a cada cerca diferent que es vol fer al programa, caldrà crear
una implementació diferent d’aquesta classe.
Programació orientada a objectes
 84
 POO i gestors de base de dades
Tot seguit, es mostra un exemple d’implementació de la classe Predicate<T>, de
manera que avalua si, donat un client, aquest té com a adreça “Adreça3”, ignorant
majúscules i minúscules (cosa que no es pot fer amb una cerca per exemple, ja que
compara textos estrictament). Normalment, per implementar aquesta classe s’usa
una classe anònima, de manera que tan bon punt es declara una variable d’aquest
tipus, ja s’indica el seu codi immediatament, en lloc de fer-ho en un fitxer a part.
1
2
3
4
5
6
7
8
9
10
11
12
13
14
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
//Declaració de la implementació de Predicate<T> com a classe anònima
Predicate p = new Predicate<Client>() {
@Override
public boolean match(Client c) {
//Codi pel criteri de cerca
return "Adreça3".equalsIgnoreCase(c.getAPostal());
}
};
//Fi de la declaració
ObjectSet<Client> result = db.query(p);
...
L’avantatge d’usar classes anònimes és que permeten incloure, dins el seu propi
codi, atributs declarats dins la mateixa classe que les conté. Això atorga gran
flexibilitat si es volen fer cerques en base a variables, i no a valors constants.
Això permet crear cerques parametritzades mitjançant objectes Predicate. Per
exemple, suposem que es volen cercar els clients que han superat cert valor en
el nombre de comandes, però aquest valor depèn d’una variable dins el codi del
vostre programa, ja que es demana pel teclat i per tant pot ser diferent en diferents
execucions. El codi següent fa tot just això.
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
importimportimportcom.db4o.*;
com.db4o.query.Predicate;
java.util.Scanner;
public class CercaParametritzada
 {
private int valor = 0;
public void cercaClients() throws Exception {
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
try {
Predicate p = new Predicate<Client>() {
@Override
public boolean match(Client c) {
return valor <= c.getNreComandes();
}
};
ObjectSet<Client> result = db.query(p);
while (result.hasNext()) {
Client cli = result.next();
System.out.println(cli);
}
} finally {
db.close();
}
}
public void setValor(int v) {
valor = v;
}
public static void main(String[] args) throws Exception {
CercaParametritzada cp = new CercaParametritzada();
Programació orientada a objectes
 85
33
34
35
36
37
38
}
}
Scanner in = new Scanner(System.in);
System.out.print("Quin és el valor mínim a cercar? ");
cp.setValor(in.nextInt());
cp.cercaClients();
POO i gestors de base de dades
En fer cerques, recordeu que a la BDOO no només hi ha aquells objectes dels
quals s’ha fet una crida store explícita, sinó que també es troben disponibles
els objectes emmagatzemats implícitament a causa d’enllaços amb altres objectes.
Per tant, a la BDOO, també es poden fer cerques sobre encàrrecs. El següent codi
permet fer una cerca parametritzada d’encàrrecs dins el sistema, en base a un valor
mínim en la seva quantitat.
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
importimportimportcom.db4o.*;
com.db4o.query.Predicate;
java.util.Scanner;
public class CercaParametritzada
 {
private int valor = 0;
public void cercaEncarrecs() {
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
try {
Predicate p = new Predicate<Encarrec>() {
@Override
public boolean match(Encarrec c) {
return valor <= c.getQuantitat();
}
};
ObjectSet<Encarrec> result = db.query(p);
while (result.hasNext()) {
Encarrec e = result.next();
System.out.println(e);
}
} finally {
db.close();
}
}
public void setValor(int v) {
valor = v;
}
public static void main(String[] args) throws Exception {
CercaParametritzada cp = new CercaParametritzada();
Scanner in = new Scanner(System.in);
System.out.print("Quin és el valor mínim a cercar? ");
cp.setValor(in.nextInt());
cp.cercaEncarrecs();
}
}
En aquests exemples, el codi per establir si cada objecte compleix o no el criteri de
la cerca és relativament simple, d’una sola línia, però el codi del mètode match pot
ser tan complex com es desitgi i basat en qualsevol informació disponible dins el
programa. Ara bé, malauradament, això vol dir que no es pot disposar de funcions
executables directament a la BD (com passava amb MAX, AVG... a SQL).
Programació orientada a objectes
 86
 POO i gestors de base de dades
3.2.4 Actualitzacions d’objectes
Podeu comprovar que s’ha
modificat executant els
exemples anteriors de cerca
amb un valor que llisti tots
els clients (com el 0).
Normalment, l’actualització d’objectes té sentit quan, primer de tot, s’afegeix un
element a la BDOO, i, en posteriors execucions del programa, el contingut dels
objectes emmagatzemats veuen modificats els seus valors al llarg del seu cicle de
vida (un client canvia la seva adreça electrònica, o va afegint encàrrecs). És el que
dóna sentit a la persistència dels objectes en una BD, en definitiva.
L’actualització d’objectes amb db4o es porta a terme usant la crida store, tal
com s’ha usat per emmagatzemar un nou objecte, només que, en aquest cas, en
lloc de ser un objecte nou, és un que ja existia prèviament a la BD. Per fer això,
primer cal carregar a memòria l’objecte des de la BD, usant una cerca, i un cop es
disposa de la seva referència, ja s’hi pot accedir per fer canvis usant els mètodes
que proporciona la seva classe, tal com es faria normalment. Aquests canvis no es
propagaran a la BD fins tornar a executar store.
Per exemple, el següent codi permet canviar l’adreça electrònica d’un client, donat
el seu nom (suposarem que el nom ha de ser únic perquè funcioni). Per millorar
la llegibilitat, s’han omès algunes comprovacions d’errors (si no s’escriu res amb
el teclat, per exemple).
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
importimportcom.db4o.*;
java.util.Scanner;
public class ModificaAElectronica {
public static void main(String[] args) throws Exception {
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
Scanner in = new Scanner(System.in);
System.out.print("Quin és nom del client? ");
String nom = in.nextLine();
//Cercar clients a la BDOO i obtenir − los a memòria com a objectes del
programa
Client qbe = new Client(nom, null, null, null);
ObjectSet<Client> clients = db.queryByExample(qbe);
if (clients.size() != 1) {
System.out.println("No es pot modificar aquest nom.");
} else {
System.out.print("Quina és la nova adreça? ");
String ad = in.nextLine();
Client c = clients.next();
c.setAElectronica(ad);
db.store(c);
}
db.close();
}
}
L’única excepció a aquest
comportament és si
l’objecte enllaçat és una
cadena de text (com s’ha
vist, precisament, a
l’exemple anterior).
Ara bé, en el cas d’objectes que contenen enllaços a altres objectes (que, a
la vegada, poden tenir enllaços a altres objectes, i així fins a molts nivells de
profunditat), el comportament de db4o no és aquest. Per poder garantir un
rendiment òptim, és obligat, en el cas d’accés a fitxers en local, canviar una mica la
declaració de l’obertura de la BD, indicant que ha d’estar configurada per acceptar
Programació orientada a objectes
 87
 POO i gestors de base de dades
actualitzacions en cascada. Això es fa usant un constructor diferent i una inicialit-
zació prèvia d’un objecte EmbeddedConfiguration. En aquesta inicialització,
cal llistar totes les classes on es vol que la BDOO controli actualitzacions en
cascada. O sigui, que es vol que, si es fa un store sobre un objecte d’aquest
tipus, també es comprovi si cal actualitzar tots els seu graf d’objectes enllaçats.
El codi d’inicialització és el que hi ha a continuació. On diu “NomClasse1”,
“NomClass2”... caldria posar el nom de la classe a controlar les actualitzacions
en cascada. Hi haurà tants registres de classes com classes es volen controlar. Per
al cas de qualsevol classe que no s’enumeri explícitament a la inicialització, amb
una línea corresponent, en fer un store, només se n’actualitzaran els atributs que
siguin tipus primitius o cadenes de text, però no altres objectes.
1
2
3
4
5
6
7
8
EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
//Configurar totes les classes on cal propagar canvis
config.common().objectClass(NomClasse1.class).cascadeOnUpdate(true);
config.common().objectClass(NomClasse2.class).cascadeOnUpdate(true);
//etc.
ObjectContainer db = Db4oEmbedded.openFile(config, "BDOOClients.db4o");
Per exemple, el codi següent permet afegir un encàrrec a un client, donat el seu
nom (suposant que aquest és únic). Com que els encàrrecs estan enllaçats dins
una llista, cal forçar les actualitzacions en cascada per a aquesta classe.
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
importimportimportcom.db4o.*;
com.db4o.config.EmbeddedConfiguration;
java.util.Scanner;
public class AfegirEncarrec {
public static void main(String[] args) throws Exception {
EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
config.common().objectClass(Client.class).cascadeOnUpdate(true);
ObjectContainer db = Db4oEmbedded.openFile(config, "BDOOClients.db4o");
Scanner in = new Scanner(System.in);
System.out.print("Quin és nom del client? ");
String nom = in.nextLine();
//Cercar clients a la BDOO i obtenir − los a memòria com a objectes del
programa
//S’usa una cerca per exemple
Client qbe = new Client(nom, null, null, null);
ObjectSet<Client> clients = db.queryByExample(qbe);
if (clients.size() != 1) {
System.out.println("No es pot modificar aquest nom.");
} else {
System.out.print("Quin és nom del producte? ");
String prod = in.nextLine();
System.out.print("Quants en vols encarregar? ");
String txtQuan = in.nextLine();
int quant = Integer.parseInt(txtQuan);
Encarrec ne = new Encarrec(prod, quant);
Client c = clients.next();
c.addComanda(ne);
db.store(c);
}
db.close();
Programació orientada a objectes
37
38
}
}
88
POO i gestors de base de dades
3.2.5 Esborrat d’objectes
Per esborrar un objecte s’aplica la idea general de les actualitzacions, però en lloc
del mètode store, cal usar el mètode delete. Primer cal recuperar l’objecte de
la BD, carregar-lo a memòria, i després esborrar-lo usant la seva referència. Per
exemple, per esborrar un client, d’entrada, es podria fer així:
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
importimportcom.db4o.*;
java.util.Scanner;
public class EsborraClient {
public static void main(String[] args) throws Exception {
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
Scanner in = new Scanner(System.in);
System.out.print("Quin és nom del client? ");
String nom = in.nextLine();
//Cercar clients a la BDOO i obtenir − los a memòria com objectes del
programa
Client qbe = new Client(nom, null, null, null);
ObjectSet<Client> clients = db.queryByExample(qbe);
if (clients.size() != 1) {
System.out.println("No es pot modificar aquest nom.");
} else {
Client c = clients.next();
db.delete(c);
}
db.close();
}
}
Si mostreu els clients que hi ha a la BDOO, veureu que ja no existeix el client
que heu escrit en executar-lo. Ara bé, aquest programa no és del tot correcte. Si
esborreu el client 3, executeu el programa que fa cerca de comandes i cerqueu
totes les comandes amb una quantitat superior a 0, observareu que les comandes
del client 3 encara estan a la BD. La crida a delete esborra, estrictament, l’objecte
associat a aquest client, però absolutament res més.
Com passava amb les actualitzacions d’objectes que enllacen altres objectes,
aquest cas s’ha de tractar d’una manera una mica especial. Malauradament, no
hi ha cap paràmetre de configuració que resolgui el problema. Per tal d’eliminar
objectes, el programador ha de fer un codi que, manualment i un per un, vagi
recorrent el mapa d’objectes i eliminant els que correspongui. Per als clients, el
programa correcte hauria de ser el següent:
1
2
3
4
5
6
7
8
9
import com.db4o.*;
import java.util.*;
public class EsborraClient {
public static void main(String[] args) throws Exception {
ObjectContainer db = Db4oEmbedded.openFile("BDOOClients.db4o");
Scanner in = new Scanner(System.in);
System.out.print("Quin és nom del client? ");
Programació orientada a objectes
 89
 POO i gestors de base de dades
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
String nom = in.nextLine();
//Cercar clients a la BDOO i obtenir − los a memòria com objectes del
programa
Client qbe = new Client(nom, null, null, null);
ObjectSet<Client> clients = db.queryByExample(qbe);
if (clients.size() != 1) {
System.out.println("No es pot modificar aquest nom.");
} else {
Client c = clients.next();
List<Encarrec> li = c.getComandes();
Iterator<Encarrec> it = li.iterator();
//anem esborrant tots els encàrrecs, un per un
while (it.hasNext()) {
Encarrec e = it.next();
db.delete(e);
}
//Ja es pot esborrar el client
db.delete(c);
}
db.close();
}
}
En aquest cas concret, la tasca no és gaire complicada, ja que només hi ha un nivell
d’objectes enllaçats, i donat un client, les seves comandes només les gestiona ell
i cap altre objecte. Ara bé, cal ser conscients que gestionar l’esborrat correcte
d’objectes enllaçats dins la BDOO es pot arribar a complicar força en casos
complexos, ja que sempre cal garantir la consistència de tot el mapa d’objectes
emmagatzemat. Si un objecte és referenciat per més d’un altre objecte, cal anar
amb molt de compte de no esborrar-lo, perquè es podrien deixar referències a null
sense voler, cosa que comportaria errors d’execució en el futur. Per exemple, si
els encàrrecs es poguessin compartir entre més d’un client, en esborrar un client
no es podrien esborrar alegrement tots els seus encàrrecs. Caldrà comprovar que
cada encàrrec només està assignat a un únic client i, si cal, l’esborrarem, però, en
cas contrari, no.
3.3 JDO (Java Data Objects)
Normalment, el motiu principal per treballar amb una BDDO és poder generar
codi que s’integri de manera natural en un programa orientat a objectes. O sigui,
poder desar i cercar directament objectes dins la base de dades, de manera que si
estan enllaçats a altres objectes mitjançant referències, també es recuperin auto-
màticament. A més a més, els mètodes d’accés a la BD ja retornen directament
els conjunts d’objectes que es vol cercar.
En contraposició, l’ús d’una BDR implica haver de fer traduccions d’objectes
a taules, i les cerques retornen files de valors de taules, que cal recuperar, i a
partir d’aquests cal instanciar les classes desitjades. Per desar un objecte, cal fer
exactament el mateix, però a la inversa: extreure’n els atributs i convertir-los a
valors dins una taula. En cas de voler desar objectes enllaçats entre si, la tasca es
pot arribar a complicar força. A part, cal fer conversions de tipus Java a SQL i
Programació orientada a objectes
Una BDOO que es basa en
JDO, per a l’accés a les
seves dades, és
JDOInstruments.
Les classes de JDO
pertanyen al paquet
javax.jdo. Aquest no
pertany a la distribució
estàndard del Java.
Objectes transitoris
S’anomena objectes transitoris
els que només són en la
memòria, però no en la BD, i
objectes buits (hollow) els que
només estan en la BD, sense
representació en la memòria.
90
 POO i gestors de base de dades
viceversa. En general, la seva integració dins el codi Java no és gens natural, ja
que s’està usant un sistema basat en taules per desar elements que, a memòria, no
són pas a taules.
Per tant, des del punt de vista de simplicitat del codi, l’ús d’una BDOO dins un
programa orientat a objectes ofereix enormes avantatges. Malauradament, el seu
grau de maduresa i estandardització no arriba ni de bon tros al que actualment
tenen les BDR, per la qual cosa no és tan senzill decidir quin tipus usar. Ara bé, el
Java disposa d’una especificació pròpia per definir la persistència d’objectes usant
una estratègia semblant a l’accés a una BDOO, però sense lligar-se a un tipus
concret de mecanisme d’emmagatzematge.
Java Data Objects (JDO) API és una interfície estàndard basada en
l’abstracció del model de persistència de Java. Les aplicacions escrites amb
l’API de JDO són independents del sistema d’emmagatzematge subjacent.
Diferents implementacions poden donar suport a diferents tipus de bases de
dades, incloent bases de dades relacionals i d’objecte, XML, arxius...
Aquesta especificació només serveix per al llenguatge Java i no és portable a
cap altre llenguatge, en contrast amb l’ODL i OQL, que intenten ser llenguatges
genèrics independents del llenguatge (com l’SQL). De fet, l’objectiu d’aquesta
especificació és haver d’obviar la necessitat d’aprendre cap llenguatge extra que
no sigui el mateix Java i res més. En aquesta secció es presenta només una breu
introducció dels aspectes generals del seu funcionament.
JDO defineix tres tipus de classes:
•Habilitades per a persistència. Representen les classes els objectes de les
quals poden passar a un estat persistent. Al llarg de l’execució de l’aplicació
poden passar de la memòria a ser emmagatzemades en la BD, i viceversa.
• Conscients de persistència. Són les classes que manipulen el tipus anterior.
Concretament, la classe JDOHelper proporciona diferents mètodes per
descobrir si un objecte concret es troba en un estat persistent o no.
• Normals. Els seus objectes no poden passar a un estat persistent, només
existiran en la memòria de l’aplicació.
Els objectes de classes habilitades per a la persistència poden passar per diferents
estats dins el seu cicle de vida, depenent de diversos factors: si només es troben
en la memòria o també estan representats en la BD, si la seva representació actual
en la memòria difereix de l’escrita en la BD, etc. La transició entre estats és
controlada per la classe PersistenceManager, que serveix d’interface primària
entre l’aplicació i la BD. Aquesta classe és, fins a cert punt, l’equivalent a la
connexió JDBC en el cas d’accés a BDR.
Totes les accions cap a la BD vindran determinades a partir de crides a mètodes
definits en aquesta classe. Alguns dels seus mètodes més significatius són:
Programació orientada a objectes
 91
• void makePersistent(Object o). Fa un objecte persistent a la BD de
manera que quan es manipulin les seves dades, quedaran desades.
• void makeTransient(Object o). Fa que un objecte deixi de ser persis-
tent en la BD i passi a ser transitori.
• void deletePersistent(Object o). Esborra un objecte persistent de
la BD.
POO i gestors de base de dades
3.3.1 Instanciació d’un objecte PersistenceManager
Per instanciar un objecte PersistenceManager no es pot usar directament una
sentència new, cal usar la classe PersistenceManagerFactory:
1
2
3
4
Properties props = new Properties();
props.put(...);
PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
PersistenceManager pm = pmf.getPersistenceManager();
Un cop disposem de l’objecte PersistenceManager, ja es pot començar a operar
amb la BDOO.
3.3.2 Interacció amb la BD
Per interactuar amb la BD, el JDO usa transaccions, per modificar l’estat d’un
objecte, i consultes, per accedir a les dades persistents. Aquesta filosofia no
és gaire diferent de la de JDBC. Les transaccions estan representades per la
classe Transaction, mentre que les consultes per la classe Query. Els objectes
d’ambdós tipus s’obtenen a partir de crides al PersistenceManager:
• Transaction currentTransaction(). Obté una transacció per modificar
l’estat d’objectes dins l’aplicació.
• Query newQuery(java.lang.Class cls). Crea una consulta, per ser execu-
tada posteriorment per accedir als objectes persistents dins la BDOO. El
paràmetre cls indica la classe esperada dels objectes que es vol consultar.
Aquest mètode es troba sobrecarregat per poder proporcionar diferents
opcions.
L’aspecte més important en el procés d’emmagatzemament i recuperació de dades
en la BD és que tot codi Java generat segueix exactament la mateixa filosofia
orientada a objectes com si sempre s’operés sobre instàncies a memòria (només
s’opera amb referències). El mecanisme de persistència és transparent.
Tot seguit es mostra com un objecte passa de ser transitori a persistent. El resultat
final és que aquest queda emmagatzemat en la BDOO.
El mètode rollback()
permet fer enrere una
transacció en cas d’error
durant el procés.
Programació orientada a objectes
 92
 POO i gestors de base de dades
1
2
3
4
5
6
7
8
9
10
11
12
Client cli = new client("Client1", "Adreça1",...);
Transaction tr;
try {
tr = pm.currentTransaction();
tr.begin();
pm.makePersistent(cli);
tr.commit();
} catch (Exception e) {
if(tx.isActive()) {
tx.rollback();
}
}
El mètode closeAll()
finalitza la consulta,
alliberant els objectes
resultants.
Per executar una consulta cal cridar el mètode execute() definit en la classe
Query. Aquest retorna una col·lecció d’objectes resultants. Per exemple, per
recuperar objectes Client emmagatzemats es faria:
1
 try {
2
 Query query = pm.getQuery(Client.class);
3
 query.setFilter("(nom == param)");
4
 query.declareParameters("String param");
5
 Collection c = (Collection)query.execute("Client1");
6
 Iterator it = c.iterator();
7
 while(it.hasNext()) {
8
 Client cli = (Client)it.next();
9
 System.out.println("S’ha trobat el client:" + cli);
10
 }
 closeAll()
11
 query.closeAll();
12
 } catch (Exception e) {
13
 ...
14
 }
Els mètodes setFilter i declareParameters permeten establir condicions
de cerca. Amb el primer s’estableix la condició d’acord amb certs paràmetres
d’entrada (param) que s’usaran en fer la consulta i es compararan amb els camps
dels objectes (nom). Amb el segon establim els tipus dels paràmetres d’entrada
(String param). En fer execute, s’indiquen els valors dels paràmetres (Client1).

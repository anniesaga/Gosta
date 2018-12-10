PRAGMA foreign_keys=OFF;

CREATE TABLE IF NOT EXISTS sponsors(id integer primary key not null, name text not null, website text, info text, fileName text);
BEGIN TRANSACTION;
INSERT INTO "sponsors" VALUES(1,'Barebells','https://barebells.com/sv/', '”Guilt free pleasures sedan 2016: Barebells proteinberikade mellanmål passar alla som vill ge sina smaklökar en riktig belöning – utan dåligt samvete. Barebells lanserades 2016 och erbjuder idag ett brett sortiment av proteinberikade alternativ till mellanmål, frukost och efterrätter – som aldrig kompromissar med smaken. Du hittar hela vårt sortiment av guilt free pleasures på barebells.se. Go ahead – obey your cravings!”','Barebells.png');
INSERT INTO "sponsors" VALUES(2,'Fisherman´s Friend','https://www.fishermansfriend.com/sv-se/', 'Embrace the strength of Fisherman’s Friend. Keep a little paper packet in your pocket and whip it out at odd moments to surprise friends, relatives and even strangers. Pop one in. Suck. Breathe deeply. A Fisherman’s Friend is powerfully strong.','Fishermansfriend.png');
INSERT INTO "sponsors" VALUES(3,'Nordstan','https://nordstan.se/', 'Vi är en shoppingupplevelse utöver det vanliga, mitt i city. Under ett och samma tak finns flagship-stores och småbutiker, restauranger och caféer. Stor variation och tillgänglighet gör att vi kan erbjuda trevlig shopping för hela familjen. Nordstan är Sveriges mest välbesökta köpcentrum med ca 200 butiker. Välkommen in för att upptäcka vårt stora utbud. Nordstan är en del av centrala Göteborg och är ett aktivitetshus med massor av butiker och events. För allas trevnad och trygghet samarbetar Nordstan löpande med myndigheter och organisationer.','Nordstan.png');
INSERT INTO "sponsors" VALUES(4,'Swift Export','http://www.swiftexport.com/', 'Välkommen till Swiftexport AB Vi importerar, distribuerar och säljer Flapjacks, Nakd Bars och Lizzi’s Granola.','Flapjack.png');
INSERT INTO "sponsors" VALUES(5,'NJIE','http://www.njie.se/', 'NJIE Foods utvecklar, marknadsför och säljer ett brett sortiment nyttiga drycker och livsmedel. Vi är kända för vår kvalitet, våra funktionella mervärden och våra nya innovationer. Framgångsfaktorn kommer från kombinationen av smartare, nyttigare produkter med goda smaker som ligger i tiden – Allt för att du som konsument ska slippa kompromissa mellan nyttigt och gott. NJIE Aloe Vera®, NJIE ProPud® och NJIE Freeno® är bara början. Du hittar oss i närmaste matbutik, hälsokostbutik, träningsanläggning och utvalda webbutiker.','Njie.png');
INSERT INTO "sponsors" VALUES(6,'Tekompaniet','https://www.tekompaniet.se/', 'Vi älskar te! ...och det är svårt att gå tillbaka till vanligt te, när du har provat våra. ​ Vår affärsidé är att med kunskap, nytänkande och personlig service vara den ledande leverantören i Sverige av kvalitetste. ​ Vi gör ekologiskt och etiskt producerat te tillgängligt för alla. Därför har vi mycket ekologiskt och Fairtradecertifierat te.','Tekompaniet.png');






COMMIT;

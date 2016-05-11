package experiments.iledWeakStrongCleanData

/**
  * Created by nkatz on 11/29/15.
  */
object RTEC_CAVIAR_Annotation {



  val LAST_TIME = 1006880 // taken from the data
  val inf = Map("inf" -> LAST_TIME)
  val SDFs = List("moving","fighting") // Statically determined fluents -- defined via holdsFor
  val DFs = List("meeting","leavingObject") // Dynamic fluents -- defined via initiatedAt

  val HLEs: Map[String,Map[(String,String),List[(Int,Int)]]] =
    Map("fighting" -> Map( ("id1","id2") -> List((1001480,1004280)), ("id2","id6") -> List((967120,967440),(968960,969800)),
      ("id3","id4") ->  List((876040,876080),(876120,880360)), ("id4","id5") -> List((897480,897520),(897560,898480),(930520,931400)),
      ("id6","id7") -> List((854840,856240),(856280,856520))),
      "meeting" -> Map( ("id0","id1") -> List((783040,790720),(831760,835640)), ("id1","id2") -> List((27040,63120),(707560,713960),(730360,751280)),
        ("id1","id6") -> List((974360,977400)), ("id2","id6") -> List((937800,939560)), ("id3","id4") -> List((1001520,1006880)), ("id4","id5") -> List((6840,24480))),
      "moving" -> Map( ("id0","id1") -> List((781960,783000),(784280,789240),(789960,790680),(811440,816120),
        (816320,816360),(816400,819880),(819920,820920),(821120,821280),(821320,821400),
        (821440,822000),(822280,822720),(822840,823040),(824920,831440),(834920,835600)),
        ("id0","id2") -> List((790880,793120),(812600,817920),(818080,818960),(819120,819240),(819440,819480),(820560,820600),(822640,822680),(823760,823960)),
        ("id0","id3") -> List((814480,817440)),
        ("id1","id2") -> List((24440,27000),(486440,490440),(490760,490880),(491000,491520),
          (492200,492840),(496320,496560),(498040,498080),(707160,707400),
          (708440,713920),(732600,745040),(812600,814920),(1000960,1001480)),
        ("id1","id3") -> List((757480,763440),(814160,817400)),
        ("id2","id3") -> List((207560,209360),(536400,538840),(814160,816360),(816400,816720)),
        ("id2","id4") -> List((755320,755480)),
        ("id2","id6") -> List((966680,966840)),
        ("id3","id4") -> List((717800,718200),(885120,886160)),
        ("id3","id5") -> List((717400,718200)),
        ("id3","id6") -> List((717400,717720),(718160,718200)),
        ("id3","id7") -> List((765440,766880)),
        ("id4","id5") -> List((2520,6200),(323440,330280),(717800,718200),(758640,759480),(895400,895520)),
        ("id4","id6") -> List((717800,718200)),
        ("id5","id6") -> List((714920,718200),(757480,764360)),
        ("id5","id7") -> List((763360,764360)),
        ("id6","id7") -> List((764080,766080),(854480,854840),(974320,974360)),
        ("id7","id9") -> List((939960,942760))),
      "leavingObject" -> Map(("id2","id4") -> List((673760,694240)), ("id3","id4") -> List((517400,533760)), ("id4","id6") -> List((554640,573440))))


  // These are the "downtime" intervals, i.e. intrvals where no HLE is recognized.
  val downTimeIntervals =
    List((0,2480), (6200,6800), (63120,66520), (66560,69960), (70000,73400), (73440,76840), (76880,80280), (80320,83720),
      (83760,87160), (87200,90600), (90640,94040), (94080,97480), (97520,100920), (100960,104360), (104400,107800), (107840,111240),
      (111280,114680), (114720,118120), (118160,121560), (121600,125000), (125040,128440), (128480,131880), (131920,135320), (135360,138760),
      (138800,142200), (142240,145640), (145680,149080), (149120,152520), (152560,155960), (156000,159400), (159440,162840), (162880,166280),
      (166320,169720), (169760,173160), (173200,176600), (176640,180040), (180080,183480), (183520,186920), (186960,190360), (190400,193800),
      (193840,197240), (197280,200680), (200720,204120), (204160,207520), (209360,212760), (212800,216200), (216240,219640), (219680,223080),
      (223120,226520), (226560,229960), (230000,233400), (233440,236840), (236880,240280), (240320,243720), (243760,247160), (247200,250600),
      (250640,254040), (254080,257480), (257520,260920), (260960,264360), (264400,267800), (267840,271240), (271280,274680), (274720,278120),
      (278160,281560), (281600,285000), (285040,288440), (288480,291880), (291920,295320), (295360,298760), (298800,302200), (302240,305640),
      (305680,309080), (309120,312520), (312560,315960), (316000,319400), (319440,322840), (322880,323400), (330280,333680), (333720,337120),
      (337160,340560), (340600,344000), (344040,347440), (347480,350880), (350920,354320), (354360,357760), (357800,361200), (361240,364640),
      (364680,368080), (368120,371520), (371560,374960), (375000,378400), (378440,381840), (381880,385280), (385320,388720), (388760,392160),
      (392200,395600), (395640,399040), (399080,402480), (402520,405920), (405960,409360), (409400,412800), (412840,416240), (416280,419680),
      (419720,423120), (423160,426560), (426600,430000), (430040,433440), (433480,436880), (436920,440320), (440360,443760), (443800,447200),
      (447240,450640), (450680,454080), (454120,457520), (457560,460960), (461000,464400), (464440,467840), (467880,471280), (471320,474720),
      (474760,478160), (478200,481600), (481640,485040), (485080,486400), (490440,490720), (490880,490960), (491520,492160), (492840,496240),
      (496560,498000), (498080,501480), (501520,504920), (504960,508360), (508400,511800), (511840,515240), (515280,517360), (533760,536360),
      (538840,542240), (542280,545680), (545720,549120), (549160,552560), (552600,554600), (573440,576840), (576880,580280), (580320,583720),
      (583760,587160), (587200,590600), (590640,594040), (594080,597480), (597520,600920), (600960,604360), (604400,607800), (607840,611240),
      (611280,614680), (614720,618120), (618160,621560), (621600,625000), (625040,628440), (628480,631880), (631920,635320), (635360,638760),
      (638800,642200), (642240,645640), (645680,649080), (649120,652520), (652560,655960), (656000,659400), (659440,662840), (662880,666280),
      (666320,669720), (669760,673160), (673200,673720), (694240,697640), (697680,701080), (701120,704520), (704560,707120), (707400,707520),
      (713960,714880), (718200,721600), (721640,725040), (725080,728480), (728520,730320), (751280,754680), (754720,755280), (755480,757440),
      (766880,770280), (770320,773720), (773760,777160), (777200,780600), (780640,781920), (790720,790840), (793120,796520), (796560,799960),
      (800000,803400), (803440,806840), (806880,810280), (810320,811400), (820920,821080), (822000,822240), (822720,822800), (823040,823720),
      (823960,824880), (831440,831720), (835640,839040), (839080,842480), (842520,845920), (845960,849360), (849400,852800), (852840,854440),
      (856520,859920), (859960,863360), (863400,866800), (866840,870240), (870280,873680), (873720,876000), (880360,883760), (883800,885080),
      (886160,889560), (889600,893000), (893040,895360), (895520,897440), (898480,901880), (901920,905320), (905360,908760), (908800,912200),
      (912240,915640), (915680,919080), (919120,922520), (922560,925960), (926000,929400), (929440,930480), (931400,934800), (934840,937760),
      (939560,939920), (942760,946160), (946200,949600), (949640,953040), (953080,956480), (956520,959920), (959960,963360), (963400,966640),
      (966840,967080), (967440,968920), (969800,973200), (973240,974280), (977400,980800), (980840,984240), (984280,987680), (987720,991120),
      (991160,994560), (994600,998000), (998040,1000920))

  // To generate the downtime intervals use the following piece of code (call getDownTimeIntervals):
  /**
  val step = 40
  def getHLEIntervals(hle: String) = {
    (RTEC_CAVIAR_Annotation.HLEs(hle) map { case (_, v) => v } toList).flatten
  }
  val allHLEs = List("moving","meeting","fighting","leavingObject")
  val HLEIntervals = allHLEs flatMap getHLEIntervals // The intervals where at least one HLE holds

  def getDownTimeIntervals = {
    val HLEIntervals = allHLEs flatMap getHLEIntervals // The intervals where at least one HLE holds
    def belongsToInterval(time: Int) = {
      // this is dangerous (very expensive), but hopefully I'll only do it once
      def f(time: Int, interval: (Int,Int)) = List.range(interval._1, interval._2 + step, step) contains time
      HLEIntervals.exists(p => f(time,p))
    }
    val generatedTimes = List.range(680,1006840+40,40)
    // create intervals of size of 85 (the average size of a positive examples interval from the RTEC annotation)
    //val grouped = generatedTimes.grouped(85).toList map (x => (x.head,x.tail.reverse.head))
    val intervalSize = 85 // create intervals of size of 85 (the average size of a positive examples interval from the RTEC annotation)
    generatedTimes.foldLeft( (List[(Int, Int)](), 0, (0,0) ) ) {
      (x, y) =>
        val intervals = x._1
        val counter = x._2
        val currentInterval = x._3
        if (belongsToInterval(y)) {
          if (currentInterval._2 != 0) {
            (intervals :+ currentInterval, 0, (y,0))
          } else {
            (intervals, 0, (y,0)) // close current interval and store it
          }
        } else {
          if (counter < intervalSize) {
            ( intervals, counter + 1, (currentInterval._1, y) )
          } else {
            (intervals :+ currentInterval, 0, (y,0)) // close current interval and store it
          }
        }
    }._1
  }

  To find the average (positive) interval size (which is 85):

  val avgIntrvSize = Utils.mean(
    allIntervals.foldLeft(List[Int]()) { (x, y) => {val size = (y._2 - y._1)/this.step ; x :+ size}} map (_.toDouble)
  ) toInt
  */



  val persons: Map[String,List[(Int,Int)]] =
    Map("id0" -> List((720,7200),(24480,26280),(66680,73560),(107360,116960),(149080,150160),
      (181440,189400),(215360,221400),(248720,254880),(280040,323000),(323040,324280),
      (362480,370440),(398920,422680),(446200,453240),(486480,495840),(537440,546520),
      (581040,587320),(622720,639200),(657240,662120),(718920,723040),(751280,752360),
      (776200,804400),(811120,823960),(824960,841240),(843000,845840),(864160,870160),
      (886200,888120),(919160,921400),(957520,959480),(989640,995880)),
      "id1" -> List((9480,20480),(24480,66640),(79560,83600),(107360,117080),(149080,158240),
        (189440,215320),(219560,237840),(254400,280000),(332160,362440),(366120,374480),
        (399720,409080),(446200,465840),(486480,507120),(536440,539160),(585720,632840),
        (662360,664360),(699720,714800),(723120,751240),(753360,794920),(811480,823080),
        (824960,839720),(842120,844080),(887160,898360),(919160,931400),(957520,979160),(997000,1006680)),
      "id2" -> List((24480,66640),(83480,96000),(107360,149040),(155880,158960),
        (206160,209360),(237880,248680),(255680,276040),(362480,365520),(417600,446160),
        (465400,486440),(498280,509840),(536440,538840),(587720,608800),(664400,699680),
        (703080,713920),(724960,751240),(751320,756680),(787000,799160),(812640,824920),
        (841280,842080),(920680,985840),(995160,1006280)),
      "id3" -> List((27280,33760),(96040,107320),(120720,149040),(159000,181400),(207600,210440),
        (325120,332040),(509880,523320),(536440,543840),(611400,619760),(717440,718200),
        (754000,768600),(804200,810160),(814200,823880),(860240,864120),(869240,886160),
        (887600,889600),(920360,922560),(980200,989600),(997720,inf("inf"))),
    "id4" -> List((1600,24440),(36280,49840),(73600,80240),(189800,189880),(323040,332120),
      (545960,562000),(613680,616240),(637480,657000),(717840,718200),(755360,759480),
      (802200,886160),(891720,903320),(923360,948200),(989640,inf("inf"))),
    "id5" -> List((2560,24440),(56480,66640),(323480,330280),(373840,398880),(528240,536400),
      (566320,581000),(614960,622680),(714840,718200),(756480,764360),(864160,872160),(890760,914560),(924240,938400)),
    "id6" -> List((613840,622680),(714960,718200),(757520,766320),(849080,861680),(885640,886160),(899680,910240),(932560,943240),(963520,977360)),
    "id7" -> List((574280,579400),(763400,776160),(842120,864120),(903920,905360),(940000,951320),(971040,981520)),
    "id8" -> List((775720,776160),(909960,918320),(937280,939600)),
    "id9" -> List((918360,919120),(938120,inf("inf"))))

  val dists: Map[(String,String,Int),List[(Int,Int)]] =
    Map(
      ("id1","id0",24) -> List((782400,790240),(811440,814160),(818000,818360),(818960,819280),(819320,819360),(824920,834920),(834960,835000)),
      ("id2","id0",24) -> List((791720,792160),(792200,792360),(792400,792440),(792520,792840),(814480,814640),(814680,815560),(815840,815880)),
  ("id2","id1",24) -> List((24440,61400),(486440,489040),(489080,489120),(707600,708400),(708480,708840),(709000,713920),(730360,733760),(733800,733920),(733960,734040),(734080,734280),(736200,751240),(813960,814000),(814040,814240),(1001280,1006280)),
  ("id3","id1",24) -> List((757960,760720),(760960,761560),(762280,762320),(762360,762440),(814160,814200),(814600,814640),(814720,814760),(815200,815240),(815320,815400)),
  ("id6","id1",24)-> List((974200,977360)),
  ("id3","id2",24)-> List((207560,208160),(208400,209360),(536400,538840),(814160,815560)),
  ("id4","id2",24)-> List((673720,674200),(692920,694240)),
  ("id6","id2",24)-> List((937800,939080),(967120,967440),(968960,969800)),
  ("id4","id3",24)-> List((517360,517440),(645520,645640),(717800,718200),(876040,876080),(876120,880520),(885680,886160),(1005360,1006840)),
  ("id5","id3",24)-> List((717400,717520),(718160,718200)),
  ("id5","id4",24)-> List((2520,24440),(323440,330280),(532760,533760),(717920,718000),(759000,759480),(897480,897520),(897560,898480),(930520,931400)),
  ("id6","id4",24)-> List((554600,556520),(901640,902480)),
  ("id6","id5",24)-> List((570960,573440),(714920,717880),(757680,757880),(758240,758440),(758600,760360),(760880,761120),(761360,761400),(761440,761840),(761880,762520),(762560,763960),(934640,935440)),
  ("id7","id5",24)-> List((763600,764360)),
  ("id9","id5",24)-> List((938080,938400)),
  ("id7","id6",24)-> List((764240,765640),(854840,856240),(856280,856520),(857000,857320),(972960,973800)),
  ("id9","id7",24)-> List((939960,942400)),
  ("id1","id0",25)-> List((782400,790360),(811440,814240),(817920,818360),(818960,819520),(824920,835040)),
  ("id2","id0",25)-> List((791680,792440),(792520,792880),(814440,815680),(815840,815960),(816000,816200),(816560,816640)),
  ("id2","id1",25)-> List((24440,61480),(486440,489200),(707520,708840),(708920,713920),(730320,734480),(735800,735840),(736040,736080),(736160,751240),(812600,814360),(1001280,1006280)),
  ("id3","id1",25)-> List((757960,760800),(760960,761600),(762080,762560),(814160,814200),(814600,814640),(814680,814800),(815200,815400),(815800,815920)),
  ("id6","id1",25)-> List((974080,977360)),
  ("id3","id2",25)-> List((207560,208200),(208320,208360),(208400,209360),(536400,538840),(814160,815680)),
  ("id4","id2",25)-> List((673720,674320),(692840,694240)),
  ("id6","id2",25)-> List((937760,939080),(967120,967440),(968920,969800)),
  ("id4","id3",25)-> List((517360,517480),(645520,645680),(717800,718200),(876040,880520),(885560,886160),(1001480,1002400),(1002440,1003640),(1005000,1006840)),
  ("id5","id3",25)-> List((717400,717800),(717920,718000),(718160,718200)),
  ("id5","id4",25)-> List((2520,24440),(323440,330280),(532680,533760),(717800,718000),(759000,759480),(896760,896800),(897480,898480),(930520,931480)),
  ("id6","id4",25)-> List((554600,556600),(718160,718200),(901600,902520)),
  ("id6","id5",25)-> List((570920,573440),(714920,718000),(757680,757960),(758040,758440),(758480,760440),(760600,760680),(760840,761120),(761200,764000),(934560,935440)),
  ("id7","id5",25)-> List((763600,764360)),
  ("id9","id5",25)-> List((938080,938400)),
  ("id7","id6",25)-> List((764200,765640),(854800,856520),(856560,856680),(857000,857320),(972960,973800)),
  ("id9","id7",25)-> List((939960,942440)),
  ("id1","id0",30)-> List((782080,790640),(811440,815040),(816800,817200),(817440,819680),(820120,820880),(821640,821920),(822320,822600),(824920,835360)),
  ("id2","id0",30)-> List((791520,793040),(814040,816840),(817600,817840),(823800,823960)),
  ("id3","id0",30)-> List((814720,816840),(817200,817240),(817320,817360)),
  ("id2","id1",30)-> List((24440,62760),(110040,110080),(486440,490120),(491240,491360),(496800,496840),(497600,497640),(707280,713920),(729960,734880),(735600,751240),(812600,814800),(1001080,1006280)),
  ("id3","id1",30)-> List((757720,763040),(814160,816120),(816240,816640),(816880,817160)),
  ("id6","id1",30)-> List((973880,977360)),
  ("id3","id2",30)-> List((207560,209360),(536400,538840),(814160,815920)),
  ("id4","id2",30)-> List((673720,674880),(692640,694240)),
  ("id6","id2",30)-> List((937400,939240),(966840,967000),(967080,967640),(968040,968280),(968720,968800),(968840,970040)),
  ("id4","id3",30)-> List((517360,517920),(645520,646280),(650160,650200),(717800,718200),(875840,880640),(885360,886160),(1000240,1006840)),
  ("id5","id3",30)-> List((717400,718200)),
  ("id7","id3",30)-> List((765600,766720)),
  ("id5","id4",30)-> List((2520,24440),(323440,330280),(532520,533760),(717800,718200),(758800,759480),(896680,896920),(897400,898640),(929640,929880),(930360,931840)),
    ("id6","id4",30)-> List((554600,556800),(717800,718200),(901520,902600)),
    ("id6","id5",30)-> List((570760,573440),(714920,718200),(757480,764280),(934480,935600)),
    ("id7","id5",30)-> List((763520,764360)),
    ("id9","id5",30)-> List((938080,938400)),
    ("id7","id6",30)-> List((764120,766000),(854600,857880),(857920,857960),(972840,973920)),
    ("id9","id7",30)-> List((939960,942640)),
    ("id1","id0",34)-> List((781960,790680),(811440,816120),(816320,816360),(816400,819880),(819920,820920),(821120,821280),(821320,821400),(821440,822000),(822280,822720),(822840,823040),(824920,835600)),
    ("id2","id0",34)-> List((790880,793120),(812600,817920),(818080,818960),(819120,819240),(819440,819480),(820560,820600),(822640,822680),(823760,823960)),
    ("id3","id0",34)-> List((814480,817440)),
    ("id2","id1",34)-> List((24440,63080),(109680,110560),(486440,490440),(490760,490880),(491000,491520),(492200,492840),(496320,498080),(499160,500760),(707160,713920),(729880,751240),(812600,814920),(1000960,1006280)),
    ("id3","id1",34)-> List((757480,763440),(814160,817400)),
    ("id6","id1",34)-> List((973760,977360)),
    ("id7","id1",34)-> List((971720,971840),(972600,973560)),
    ("id3","id2",34)-> List((207560,209360),(536400,538840),(814160,816360),(816400,816720)),
    ("id4","id2",34)-> List((673720,675040),(692440,694240),(755320,755480)),
    ("id6","id2",34)-> List((936880,939520),(966680,967640),(967920,968560),(968600,968800),(968840,970240)),
    ("id4","id3",34)-> List((517360,518040),(645520,648040),(648160,648480),(648560,648640),(648680,651120),(717800,718200),(875720,880720),(885120,886160),(999840,1006840)),
    ("id5","id3",34)-> List((717400,718200)),
    ("id6","id3",34)-> List((717400,717720),(718160,718200)),
    ("id7","id3",34)-> List((765440,766880)),
    ("id5","id4",34)-> List((2520,24440),(323440,330280),(532360,533760),(717800,718200),(758640,759480),(895400,895600),(895920,896040),(896560,897200),(897320,898880),(928280,928520),(928920,928960),(929360,929440),(929480,929960),(930040,930160),(930280,931880)),
    ("id6","id4",34)-> List((554600,556920),(717800,718200),(901440,902760)),
    ("id6","id5",34)-> List((570640,573440),(714920,718200),(757480,764360),(934440,935760),(935840,935880)),
    ("id7","id5",34)-> List((763360,764360)),
    ("id9","id5",34)-> List((938080,938400)),
    ("id7","id6",34)-> List((764080,766080),(854480,858000),(972800,974080),(974320,974360)),
    ("id9","id7",34)-> List((939960,942760))


    )


  val distsSymmetric: Map[(String,String,Int),List[(Int,Int)]] =
    Map(
      ("id0","id1",30) -> List((782080,790640),(811440,815040),(816800,817200),(817440,819680),(820120,820880),(821640,821920),(822320,822600),(824920,835360)),
  ("id0","id2",30) -> List((791520,793040),(814040,816840),(817600,817840),(823800,823960)),
  ("id0","id3",30) -> List((814720,816840),(817200,817240),(817320,817360)),
  ("id1","id2",30) -> List((24440,62760),(110040,110080),(486440,490120),(491240,491360),(496800,496840),(497600,497640),(707280,713920),(729960,734880),(735600,751240),(812600,814800),(1001080,1006280)),
  ("id1","id3",30) -> List((757720,763040),(814160,816120),(816240,816640),(816880,817160)),
  ("id1","id6",30) -> List((973880,977360)),
  ("id2","id3",30) -> List((207560,209360),(536400,538840),(814160,815920)),
  ("id2","id4",30) -> List((673720,674880),(692640,694240)),
  ("id2","id6",30) -> List((937400,939240),(966840,967000),(967080,967640),(968040,968280),(968720,968800),(968840,970040)),
  ("id3","id4",30) -> List((517360,517920),(645520,646280),(650160,650200),(717800,718200),(875840,880640),(885360,886160),(1000240,1006840)),
  ("id3","id5",30) -> List((717400,718200)),
  ("id3","id7",30) -> List((765600,766720)),
  ("id4","id5",30) -> List((2520,24440),(323440,330280),(532520,533760),(717800,718200),(758800,759480),(896680,896920),(897400,898640),(929640,929880),(930360,931840)),
  ("id4","id6",30)->List((554600,556800),(717800,718200),(901520,902600)),
  ("id5","id6",30)->List((570760,573440),(714920,718200),(757480,764280),(934480,935600)),
  ("id5","id7",30)->List((763520,764360)),
  ("id5","id9",30)->List((938080,938400)),
  ("id6","id7",30) -> List((764120,766000),(854600,857880),(857920,857960),(972840,973920)),
  ("id7","id9",30)->List((939960,942640)))






}

package all.utils


import java.io.File
import java.util

import com.typesafe.scalalogging._
import all.globalValues.GlobalValues
import scala.collection.JavaConverters._
import all.core.{ Core, Crossvalidation }
import all.parsers.ASPResultsParser
import all.structures.Rules._
import all.structures._
import jep.Jep
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ Await, Future }
import scala.sys.process.{ ProcessIO, _ }
import all.structures.Examples._
import scala.io.Source


object ASP extends ASPResultsParser with LazyLogging {

   /**
    * Transforms input to an ASP program. The program is written in an output file that is passed to the ASP solver.
    * the writeTo file is the only non-optional parameter of the method.
    *
    * @param writeToFile @tparam String path to file where the ASP program is written.
    * @param program @tparam List[String] an (optional) set of ground or non-ground rules and/or ground facts.
    * @param generateDirectives @tparam List[String] an (optional) list containing declarations for atoms to be be generated during the computation
    * of answer sets.
    * @example of such input:
    *
    * List("father(X,Y):person(X):person(Y)","grandfather(X,Y):person(X):person(Y)")
    *
    * Such a list is transformed into the "generate" part of the program:
    *
    * {father(X,Y):person(X):person(Y), grandfather(X,Y):person(X):person(Y)}.
     * @param generateAtLeast @tparam Int an (optional) lower bound for the number of generated atoms to be included in an answer set.
    * @param generateAtMost @tparam Int an (optional) upper bound for the number of generated atoms to be included in an answer set.
    * @param minimizeStatements @tparam List[String] an (optional) list of atoms whose instances in an anser set should be minimized.
    * @example of such input:
    *
    * List("father(X,Y)","grandfather(X,Y)"))
    *
    * Such a list is transformed into a minimize statement:
    *
    * #minimize{father(X,Y),grandfather(X,Y)}.
     * @param maximizeStatements @tparam List[String] similar as above for maximize directives.
    * @param constraints @tparam List[List[String]] a set of integrity constraints. Example:
    *
    * List(List("father(X,Y)","mother(X,Y)"), List("father(X,Y)","not male(X)"))
    *
    * Such input is transformed to integrity constraints in the ASP program:
    *
    * :- father(X,Y), mother(X,Y).
    * :- father(X,Y), not male(X).
     * @param show @tparam List[String] an (optional) list of atoms that are to be displayed. All other atoms in an answer set are hidden.
    * A #hide directive is generated is this list is not empty.
     * @example of such input:
    *
    * List("father(X,Y)","mother(X,Y)") or
    *
    * List("father/2","mother2")
    *
    * Such input is transformed into
    *
    *
    * #hide.
    * #show father(X,Y).
    * #show mother(X,Y)
     * @param extra @tparam List[String] any extra knowledge, that is simply written in the ASP file
    */

   /*
   def toASPprogram(program: List[String] = Nil,
                    generateDirectives: List[String] = Nil,
                    generateAtLeast: Int = 1000000000,
                    generateAtMost: Int = 1000000000,
                    minimizeStatements: List[String] = Nil,
                    maximizeStatements: List[String] = Nil,
                    constraints: List[List[String]] = Nil,
                    show: List[String] = Nil,
                    extra: List[String] = Nil,
                    writeToFile: String): Any = {

      // Create generate-and-test statements
      val generates = 
         genAndTestDirectives(generateDirectives,generateAtLeast,generateAtMost,writeToFile).mkString("\n")
      
      // Create the minimize statements
      val minStatement = minimizeStatements match { // This is a single string
         case Nil => ""
         case _   => "#minimize{"+minimizeStatements.mkString(",")+"}.\n"
      }

      // Create the maximize statements
      val maxStatement = maximizeStatements match { // This is a single string
         case Nil => ""
         case _   => "#maximize{"+maximizeStatements.mkString(",")+"}.\n"
      }

      // Create integrity constraints
      val constrs = constraints match { // This is a list of strings
         case Nil => ""
         case _ => (constraints map (x => ":- "+x.mkString(",")+".")).mkString("\n")
      }

      // Create the show/hide directives:
      val (hideDir, showDirs) = show match {
         case Nil => ("", "")
         case _   => ("#hide.\n", (for (x <- show) yield "#show " + x + ".").mkString("\n") )
      }


      
      val all = 
         List(program.mkString("\n"),generates,minStatement,
            maxStatement,constrs,hideDir,showDirs,extra.mkString("\n")).mkString("\n")
      
            val debug = 
         List(generates,minStatement,
            maxStatement,constrs,hideDir,showDirs,extra.mkString("\n")).mkString("\n")      
            
      Utils.writeLine(all, writeToFile, "append")
      logger.debug("\nAbduction program:\n" + all + "\n")

   }


   def genAndTestDirectives(gens: List[String],atLeast:Int,atMost: Int,file: String) = {
      val genStatems = (gens, atLeast, atMost) match {
         case x @ (Nil, _, _)                            => List()
         case x @ (head :: tail, 1000000000, 1000000000) => for (e <- x._1) yield "{" + e + "}."
         case x @ (head :: tail, lower, 1000000000)      => (head :: tail).map(y => "$lower {" + y + "}.")
         case x @ (head :: tail, 1000000000, upper)      => (head :: tail).map(y => "0 {" + y + "} $upper.")
         case x @ (head :: tail, lower, upper)           => (head :: tail).map(y => "$lower {" + y + "} $upper.")
      }
      //Utils.writeToFile(new java.io.File(file), "append")(p => genStatems foreach (p.println))
      genStatems
   }
*/

   def toASPprogram(program: List[String] = Nil,
                    generateDirectives: List[String] = Nil,
                    generateAtLeast: Int = 1000000000,
                    generateAtMost: Int = 1000000000,
                    minimizeStatements: List[String] = Nil,
                    maximizeStatements: List[String] = Nil,
                    constraints: List[List[String]] = Nil,
                    show: List[String] = Nil,
                    extra: List[String] = Nil,
                    writeToFile: String): Any = {

      Utils.clearFile(writeToFile) // clear here, append everywhere else.
      Utils.writeToFile(new java.io.File(writeToFile), "append")(p => program foreach (p.println))
      val genStatems = (generateDirectives, generateAtLeast, generateAtMost) match {
         case x @ (Nil, _, _)                            => List()
         case x @ (head :: tail, 1000000000, 1000000000) => for (e <- x._1) yield "{" + e + "}."
         case x @ (head :: tail, lower, 1000000000)      => (head :: tail).map(y => "$lower {" + y + "}.\n")
         case x @ (head :: tail, 1000000000, upper)      => (head :: tail).map(y => "0 {" + y + "} $upper.\n")
         case x @ (head :: tail, lower, upper)           => (head :: tail).map(y => "$lower {" + y + "} $upper.\n")
      }
      Utils.writeToFile(new java.io.File(writeToFile), "append")(p => genStatems foreach (p.println))
      val minStatement = minimizeStatements match { // This is a single string
         case Nil => ""
         case _   => "#minimize{ " + minimizeStatements.mkString(",") + "}.\n"
      }
      val maxStatement = maximizeStatements match { // This is a single string
         case Nil => ""
         case _   => "#maximize{ " + maximizeStatements.mkString(",") + "}.\n"
      }
      val constrs = constraints match { // This is a list of strings
         case Nil => List("")
         case _   => for (x <- constraints) yield ":- " + x.mkString(",") + ".\n"
      }
      Utils.writeLine(minStatement, writeToFile, "append")
      Utils.writeLine(maxStatement, writeToFile, "append")
      Utils.writeToFile(new java.io.File(writeToFile), "append")(p => constrs foreach (p.println))
      val showDirs = show match {
         case Nil => ""
         case _   => "\n#show.\n"+(show map (x => s"\n#show $x.")).mkString("\n")
      }
      Utils.writeLine(showDirs, writeToFile, "append")
      Utils.writeToFile(new java.io.File(writeToFile), "append")(p => extra foreach (p.println))

     Utils.writeToFile(new java.io.File(writeToFile), "append")(p => showDirs foreach (p.println))
      val debug = scala.io.Source.fromFile(writeToFile).mkString
      logger.debug(s"\nASP Input:\n \n$debug\n")
   }

   /**
    * This generates a helper ASP program to extract the mode declaration atoms (if any) that match
    * each atom in an answer set returned by the solver. This helps to process the atoms and populate
    * the objects the are constructed from them as their internal representations. In practice this
    * program computes theta-subsumption between literals.
    *
    * @example This is a (slightly adapted) example from the E.coli case study from:
    *
    * Ray, O. (2009). Nonmonotonic abductive inductive learning. Journal of Applied Logic, 7(3), 329-340.
    *
    * %% Given Mode declarations:
    * -----------------------------------------------
    * modeh(happens(use(#sugar),+time)).
    * modeh(happens(add(#sugar),+time)).
    * modeb(holdsAt(available(#sugar),+time)).
    * modeb(not_holdsAt(available(#sugar),+time)).
    * -----------------------------------------------
    * %% Generate the following program:
    * ----------------------------------------------------------------------------------------------------------
    * mode(1,happens(use(X),Y)) :- sugar(X),time(Y). %% one atom for each mode, counting them with the 1st arg.
    * mode(2,happens(add(X),Y)) :- sugar(X),time(Y).
    * mode(3,holdsAt(available(X),Y)) :- sugar(X),time(Y).
    * mode(4,not_holdsAt(available(X),Y)) :- sugar(X),time(Y).
    *
    * modeCounter(1..4).
    *
    * matchesMode(ModeCounter,Atom,Mode) :-
    *     mode(ModeCounter,Atom), mode(ModeCounter,Mode), true(Atom), Atom = Mode.
    *
    * %% Add one such rule for each predicate (mode atom) you want to query. The purpose is to
    * %% is to generate matchesMode/3 instances only for atoms that are included in an
    * %% answer set (i.e. true atoms), in order to avoid huge amounts of irrelevant info.
    *
    * true(happens(use(X),Y)) :- happens(use(X),Y).
    * true(happens(add(X),Y)) :- happens(add(X),Y).
    * true(holdsAt(available(X),Y)) :- holdsAt(available(X),Y).
    * true(holdsAt(not_available(X),Y)) :- holdsAt(not_available(X),Y).
    *
    * #hide.
    * #show matchesMode/3.
    * ---------------------------------------------------------------------------------------------------------
    *
    * An atom 'matchesMode(m,atom,_)'' in an answer set of this program is interpreted as a true atom
    * that matches with mode atom 'm'.
    *
    *
    */
   def matchModesProgram(queryModePreds: List[Literal]): List[String] = {
      val modeDecl: List[String] = for (
         x <- queryModePreds;
         y <- List.range(1, queryModePreds.length + 1) zip queryModePreds
      ) yield "mode(" + y._1 + "," + x.tostring + "," + y._2.tostring + ") :- " + x.typePreds.mkString(",") + "."
      val modeCount: String = "modeCounter(1.." + queryModePreds.length + ")."
      val clause = """matchesMode(ModeCounter,Atom,Mode) :- 
       mode(ModeCounter,Atom, Mode), true(Atom), Atom = Mode."""
      val trues: List[String] = for (x <- queryModePreds) yield "true(" + x.tostring + ")" + " :- " + x.tostring + "."
      val program = modeDecl ++ List(modeCount) ++ List(clause) ++ trues ++ List("\n#show matchesMode/3.")
      //program.foreach(println)
      program
   }

   /**
    * Calls the ASP solver and returns the results.
    *
    * @param task an indicator for what we want to do with the solver, See the code for details. New task may be
    * added easily by providing the proper input to the ASP solver.
    * @todo Error handling needs fixing. Right now we do not intercept
    * the stderr stream, but we flag an error in case of an empty model.
    *
    * FIX THIS!!!
    */



   def solve(task: String = "",
             useAtomsMap: Map[String, Literal] = Map[String, Literal](),
             aspInputFile: java.io.File = new File(""),
             examples: Map[String, List[String]] = Map(),
             fromWeakExmpl:Boolean = false,
             jep: Jep): List[AnswerSet] = {

  /*

      val buffer = new StringBuffer()
      val command = task match {
         case Core.ABDUCTION             => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         case Core.GET_QUERIES            => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         case Core.DEDUCTION             => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         case Core.XHAIL                 => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         case Core.ILED                  => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         case Core.CHECKSAT              => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         // I'm not sure if finding all optimals is necessary here
         //case Core.FIND_ALL_REFMS     => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}", "solveMode=optN")
         case Core.FIND_ALL_REFMS     => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
         case Core.INFERENCE          => Seq("python",  Core.ASPHandler, s"aspfile=${aspInputFile.getCanonicalPath}")
        //case "getGroundings"         => Seq(Core.aspSolverPath + "/./clingo", Core.bkFile, aspInputFile.getCanonicalPath, "0", "--asp09")
         //case "useWithKNNClassifier2" => Seq(Core.aspSolverPath + "/./clingo", Core.bkFile, aspInputFile.getCanonicalPath, "0", "--asp09")
         //case "generate-aleph-negatives" => Seq(Core.aspSolverPath + "/./clingo", alternativePath, "0", "--asp09")
      }

      val strCommand = command.mkString(" ")
  /*
      def formHypothesis(answerSet: List[String], hypCount: Int) = {
         answerSet match {
            case Nil =>
            case _ =>
               val rules = getNewRules(answerSet,useAtomsMap)
               val rulesWithTypes = rules.clauses map (x => x.withTypePreds())
               val f = Utils.getTempFile(prefix = "theory", suffix = ".lp",deleteOnExit = true)
               Utils.writeToFile(f, "overwrite")(p =>
                  rulesWithTypes foreach (x => p.println(x.tostring)))
               val (tps, fps, fns, precision, recall, fscore) = {
                 //val c = new Crossvalidation(examples, rulesWithTypes)
                 //c.out
               }
               logger.info(
                  "\n---------------------------------\n" +
                  s"Enumerated hypothesis $hypCount:\n" +
                  "---------------------------------\n" +
                  (rules map (x => x.tostring)).mkString("\n") +
                  s"\n\nTPs: $tps\n" + s"FPs: $fps\n" + s"FNs: $fns\n" +
                  s"Precision: $precision\n" + s"Recall: $recall\n" + s"F-score: $fscore\n" +
                  "---------------------------------\n"
               )
         }
      }
      */
      var hypCount: Int = 0
      var lout = new ListBuffer[AnswerSet]

      val processLine = (x: String, y: Int) => parseAll(aspResult, x.replaceAll("\\s", "")) match {
         case Success(result, _) =>
            //formHypothesis(result, y)
            hypCount += 1
            if (result != List()) lout = new ListBuffer[AnswerSet]() += AnswerSet(result)
         case f                  => None
      }

      val processLine1 = (x: String) => parseAll(aspResult, x.replaceAll("\\s", "")) match {
         case Success(result, _) =>
            if (result != List()) {
               lout =task match {
                  case Core.FIND_ALL_REFMS => lout += AnswerSet(result) // keep all solutions
                  case _ => new ListBuffer[AnswerSet]() += AnswerSet(result) // keep only the last solution
               }
               logger.debug(s"$task:  ${lout.mkString(" ")}")
            }
         case f                  => None
      }

      val dispatch = (x: String) => task match {
         case Core.XHAIL => processLine(x, hypCount)
         case _       => processLine1(x)
      }

      task match {
         case "Not currently used" =>
            val pio = new ProcessIO(_ => (),
               stdout => scala.io.Source.fromInputStream(stdout).getLines.foreach(x => dispatch(x)),
               stderr => scala.io.Source.fromInputStream(stderr).getLines.foreach(println))
            command.run(pio)
            lout.toList

         case _ => //allTasks
            //val out = command lines_! ProcessLogger(buffer append _)
            val out = command.lineStream_!
            //out.foreach(println)
            if(out.head == Core.UNSAT) {
               task match {
                  case Core.CHECKSAT => return List(AnswerSet.UNSAT)
                  case _ =>
                     task match {
                       // we need this in order to remove inconsistent weak support rules
                       case Core.FIND_ALL_REFMS => return List(AnswerSet.UNSAT)
                       case (Core.ABDUCTION | Core.ILED) =>
                         // some times a kernel cannot be created from garbage (weak) data points
                         // but we don't want a failure in this case. Some holds when generalizing
                         // a kernel from a weak data point, to gen a new rule, but end up in an
                         // UNSAT program. We don't want a crash in this case either, we simply want
                         // to quit learning from the particular example and move on.
                         if (fromWeakExmpl) {
                           if (task == Core.ILED) logger.info("Failed to learn something from that...")
                           return Nil
                         } else {
                           logger.error(s"\nTask: $task -- Ended up with an UNSATISFIABLE program")
                           throw new RuntimeException(s"\nTask: $task -- Ended up with an UNSATISFIABLE program")
                         }
                       case _ =>
                         logger.error(s"\nTask: $task -- Ended up with an UNSATISFIABLE program")
                         throw new RuntimeException(s"\nTask: $task -- Ended up with an UNSATISFIABLE program")
                     }

               }
            }
            //out.foreach(x => processLine1(x))
            out.foreach(x => dispatch(x))

      }
      lout.toList
*/

     solveASP(jep,task,aspInputFile.getCanonicalPath,fromWeakExmpl)

   }






  def solveASP(jep: Jep, task: String, aspFile: String, fromWeakExmpl: Boolean = false): List[AnswerSet] = {

    jep.runScript(GlobalValues.ASPHandler)
    val file = aspFile.asInstanceOf[AnyRef]
    val _task = task.asInstanceOf[AnyRef]
    val solvem =
      if(task == GlobalValues.ABDUCTION && GlobalValues.glvalues("iter-deepening").toBoolean) {
        s"${GlobalValues.glvalues("iterations")}".asInstanceOf[AnyRef]
      } else {
        "all".asInstanceOf[AnyRef]
      }

    val solve = jep.invoke("run", file, solvem, _task).asInstanceOf[util.HashMap[String, util.ArrayList[String]]]

    //jep.eval(s"x = run('$file', '$solvem', '${_task}')")
    //val solve = jep.getValue("x").asInstanceOf[util.HashMap[String, util.ArrayList[String]]]
    //jep.eval("del x")

    /*
    jep.eval(s"asp = ASPHandler('$file', '$solvem', '${_task}')")
    jep.eval("asp.solve()")
    jep.eval(s"x = run(asp)")
    val solve = jep.getValue("x").asInstanceOf[util.HashMap[String, util.ArrayList[String]]]
    //jep.eval("asp.clear()")
    jep.eval("del x")
    jep.eval("del asp")
    */

    val status = solve.get("status").asScala.head
    //val status = "SAT"
    val _models = solve.get("models").asScala.toList.reverse // we need the last (and most minimal) models first
    //val _models = List[String]()
    /*
    if (task == GlobalValues.SCORE_RULES) {
      val grnd = solve.get("grnd").asScala.head
      GlobalValues.grnd = grnd.toInt
    }
    */
    //=========================================================================
    // This is a quick fix to get the result for abduction when
    // perfect-fit=false. In this case numerous models are
    // returned, and often the empty one is the optimal (smallest).
    // But the empty one will be filtered out by the code val models = _models
    // and we'll end up doing extra stuff with other models for no real reason
    // I'm just adding this as a quick & dirty fix to make sure that nothing
    // else breaks.
    //=========================================================================
    //-------------------------------------------------------------------------
    if (task == GlobalValues.ABDUCTION && _models.head == "") return Nil
    //-------------------------------------------------------------------------
    val models = _models filter (x => x.replaceAll("\\s", "") != "")
    if(status == GlobalValues.UNSAT) {
      task match {
        case GlobalValues.CHECKSAT => return List(AnswerSet.UNSAT)
        case _ =>
          task match {
            // we need this in order to remove inconsistent weak support rules
            case GlobalValues.FIND_ALL_REFMS => return List(AnswerSet.UNSAT)
            // searching alternative abductive explanations with iterative search
            case GlobalValues.SEARCH_MODELS => return List(AnswerSet.UNSAT)
            case GlobalValues.INFERENCE => return List(AnswerSet.UNSAT)
            case GlobalValues.SCORE_RULES=> return List(AnswerSet.UNSAT)
            case GlobalValues.GROW_NEW_RULE_TEST=> return List(AnswerSet.UNSAT)
            case (GlobalValues.ABDUCTION | GlobalValues.ILED) =>
              // some times a kernel cannot be created from garbage (weak) data points
              // but we don't want a failure in this case. Same holds when generalizing
              // a kernel from a weak data point, to gen a new rule, but end up in an
              // UNSAT program. We don't want a crash in this case either, we simply want
              // to quit learning from the particular example and move on.
              if (fromWeakExmpl) {
                if (task == GlobalValues.ILED) logger.info("Failed to learn something from that...")
                return Nil
              } else {
                logger.error(s"\nTask: $task -- Ended up with an UNSATISFIABLE program")
                val program = Source.fromFile(aspFile).getLines.toList.mkString("\n")
                throw new RuntimeException(s"\nTask: $task -- Ended up with an UNSATISFIABLE program:\n$program")
              }
            case _ =>
              logger.error(s"\nTask: $task -- Ended up with an UNSATISFIABLE program")
              val program = Source.fromFile(aspFile).getLines.toList.mkString("\n")
              throw new RuntimeException(s"\nTask: $task -- Ended up with an UNSATISFIABLE program:\n$program")
          }

      }
    }
    if (models.isEmpty) Nil
    else models map (x => AnswerSet(x.split(" ").toList))
  }








   /**
    * Creates and Writes the contents of the ASP file for SAT checking
     *
     * @param theory
    * @param example
    * @return the path to the file to be passed to the ASP solver
    */

   def check_SAT_Program(theory: Theory, example: Example): String = {
      val e = (example.annotationASP ++ example.narrativeASP).mkString("\n")
      val exConstr = getCoverageDirectives(withCWA = Core.glvalues("cwa")).mkString("\n")
      val t = theory.map(x => x.withTypePreds().tostring).mkString("\n")
      val f = Utils.getTempFile("sat",".lp",deleteOnExit = true)
      Utils.writeToFile(f, "append")(
         p => List(e,exConstr,t,s"\n#include "+"\""+Core.bkFile+"\".\n") foreach p.println
      )
      f.getCanonicalPath
   }

  def iterSearchFindNotCoveredExmpls(theory: Theory, example: Example) = {
    val e = (example.annotationASP ++ example.narrativeASP).mkString("\n")
    val varbedExmplPatterns = Core.examplePatterns map (x => x.varbed.tostring)
    val constr = varbedExmplPatterns.flatMap(x => List(s"posNotCovered($x) :- example($x), not $x.", s"negsCovered($x):- negExample($x), $x.")).mkString("\n")
    val t = theory.map(x => x.withTypePreds().tostring).mkString("\n")
    val show = s"\n#show.\n#show posNotCovered/1.\n#show negsCovered/1."
    val program = e+t+constr+show
    val f = Utils.getTempFile("sat",".lp",deleteOnExit = true)
    Utils.writeLine(program,f.getCanonicalPath,"overwrite")
    f.getCanonicalPath
  }


  /** @todo Refactor these, reduce code **/

  def isConsistent_program(theory: Theory, example: Example): String = {
    // If annotation is given here, negatives may be covered by inertia.
    // On the other hand, if the annotation is omitted then during inference
    // a correct rule will (correctly) entail positives that will be treated
    // as negatives (due to the lack of annotation). To overcome the issue,
    // the annotation is provided but inertia is defused during inference.
    val e = (example.annotationASP ++ example.narrativeASP).mkString("\n")
    //val e = (example.narrativeASP).mkString("\n")
    val exConstr = getCoverageDirectives(checkConsistencyOnly = true).mkString("\n")
    val t = theory.map(x => x.withTypePreds().tostring).mkString("\n")
    val f = Utils.getTempFile("isConsistent",".lp",deleteOnExit = true)
    Utils.writeToFile(f, "append")(
      p => List(e,exConstr,t,s"\n#include "+"\""+Core.bkFileNoInertia+"\"\n.") foreach p.println
    )
    f.getCanonicalPath
  }


   def isConsistent_program_Marked(theory: Theory, example: Example): String = {
     // If annotation is given here, negatives may be covered by inertia.
     // On the other hand, if the annotation is omitted then during inference
     // a correct rule will (correctly) entail positives that will be treated
     // as negatives (due to the lack of annotation). To overcome the issue,
     // the annotation is provided but inertia is defused during inference.
     // This method marks each rule in order to track the derivation of
     // negative examples from the particular rule.
     val e = (example.annotationASP ++ example.narrativeASP).mkString("\n")
     //val exConstr = getCoverageDirectives(checkConsistencyOnly = true).mkString("\n")
     val markInit = "\ninitiatedAt(F,T) :- marked(I,J,initiatedAt(F,T)),rule(I),supportRule(J).\n"
     val markTerm = "\nterminatedAt(F,T) :- marked(I,J,terminatedAt(F,T)),rule(I),supportRule(J).\n"
     val show = "\n#show negsCovered/3.\n"

     val markThem =
       (for( (c,i) <- theory.clauses zip List.range(0,theory.clauses.length);
             (cs,j) <- c.supportSet.clauses zip List.range(0,c.supportSet.clauses.length);
             y = cs.withTypePreds();
             marked = Clause(Literal(functor="marked", terms=List(Constant(i.toString), Constant(j.toString),y.head)),body = y.body))
          yield marked.tostring).mkString("\n")

     val ruleGen = s"rule(0..${theory.clauses.length}).\n"

     val varbedExmplPatterns = for (x <- Core.examplePatterns) yield x.varbed.tostring
     val coverageConstr = varbedExmplPatterns.map(x =>
       s"\nnegsCovered(I,J,$x):- marked(I,J,$x), not example($x),rule(I),supportRule(J).\n").mkString("\n")

     val ssRuleGne = s"supportRule(0..${theory.clauses.foldLeft(0){
       (x,y) =>
         val newMax = y.supportSet.clauses.length
         if (newMax > x) newMax else x
     }}).\n"

     val f = Utils.getTempFile("isConsistent",".lp",deleteOnExit = true)
     Utils.writeToFile(f, "append")(
       p => List(e,coverageConstr,markThem,markInit,markTerm,s"\n#include "+"\""+Core.bkMarkedFile+"\".\n",ruleGen,ssRuleGne,show) foreach p.println
     )
     f.getCanonicalPath
   }





  /**
    * Performs inference, returns the results (ground instances
    * of example pattern atoms)
    *
    * @param p a Clause or Theory
    * @param e an example (Herbrand interpretation)
    *
    *
    *
    */

   def inference(p: Expression, e: Example, jep: Jep): AnswerSet = {
     val f = (p: Expression) => p match {
        case x: Clause => x.withTypePreds().tostring
        case x: Theory => (x.clauses map (z => z.withTypePreds().tostring)).mkString("\n")
     }
     val file = Utils.getTempFile("inference",".lp",deleteOnExit = true)
     val aspProgram =
        (e.toMapASP("annotation") ++ e.toMapASP("narrative")).mkString("\n")+
         "\n" + f(p)+ Core.varbedExamplePatterns.map(x => s"\n#show ${x.tostring}:${x.tostring}.").mkString("\n")
     ASP.toASPprogram(program = List(aspProgram),
       writeToFile = file.getCanonicalPath)
     val covers = ASP.solve("inference",aspInputFile = file, jep=jep)
     covers.head
   }


   /**
    *
    * @param kernelSet analyzed using use/2 predicates
    * @param priorTheory analyzed using use/3 predicates
    * @param retained this (optional) program is used for inference as is.
    * @param findAllRefs this is a 2-tuple consisting of a single rule R and one
    *                    of R's support set rules. R is analyzed using use3
    *                    predicates. This is used in order to search the particular
    *                    support set rule for all of R's refinements w.r.t. a set of
    *                    examples.
    * @param examples
    * @param aspInputFile
    * @param learningTerminatedAtOnly
    * @return
     * @todo Simplify/refactor this
    */


   def inductionASPProgram(kernelSet: Theory = Theory(),
                           priorTheory: Theory = Theory(),
                           retained: Theory = Theory(),
                           findAllRefs: (Clause,Clause) = (Clause.empty,Clause.empty), // used only with Rules.getRefinedProgram.search method
                           examples: Map[String, List[String]] = Map(),
                           aspInputFile: java.io.File = new java.io.File(""),
                           learningTerminatedAtOnly: Boolean = false,
                           use3WithWholeSupport: Boolean = false,
                           withSupport: String = "fullSupport") = {

      val (defeasibleKS, use2AtomsMap) = kernelSet.use_2_split
     /*
     logger.debug("\nDefeasible Kernel Set:\n" +
        defeasibleKS.clauses.map(x => x.tostring).mkString("\n"))
      logger.debug("Use atoms -- kernel literals map:\n" +
        use2AtomsMap.map(x => x._1 + "->" + x._2.tostring).mkString("\n"))
     */

     val (defeasiblePrior, use3AtomsMap, use3generates) =
       if (use3WithWholeSupport) priorTheory.use_3_split_all(withSupport=withSupport)
       else priorTheory.use_3_spilt_one(withSupport=withSupport)
     /*
     logger.debug("\nDefeasible prior theory:\n" +
        defeasiblePrior.clauses.map(x => x.tostring_debug).mkString("\n"))
      logger.debug("Use atoms -- kernel literals map:\n" +
        use3AtomsMap.map(x => x._1 + "->" + x._2.tostring).mkString("\n"))
     */

     // This is used only to analyse one particular support set rule in
     // order to search for all refinements that may be derived from it.
     // It is necessary for the search method of Rules.getRefinedProgram
     // to work
     val (defeasible_Rule, use3AtomsMap_Rule, use3generates_Rule) =
         findAllRefs._1.use_3_split_one(1,findAllRefs._2)

     val varbedExmplPatterns = Core.examplePatterns map (x => x.varbed)

     val coverageConstr = Core.glvalues("perfect-fit") match {
       case "true" => getCoverageDirectives(learningTerminatedAtOnly=learningTerminatedAtOnly)
       case "false" => List()
       case _ => throw new IllegalArgumentException("Unspecified parameter for perfect-fit")
     }

     val generateUse2 =
       if (!use2AtomsMap.isEmpty) {
         (for ( (cl, i) <- kernelSet.clauses zip
           List.range(1, kernelSet.clauses.length + 1)
         ) yield "{use2(" + i + ",0.." + cl.body.length + ")}.").
           mkString("\n")
       } else { "" }

     val generateUse3 = use3generates.mkString("\n")

     val program =
         defeasibleKS.extend(defeasiblePrior).
           extend(defeasible_Rule).clauses.map(x => x.tostring_debug) ++
           coverageConstr ++
           List(generateUse2, generateUse3, use3generates_Rule) ++
           retained.clauses.map(p => p.withTypePreds().tostring)

     val f = (x: Literal) => "1," + (x.variables map (y => y.tostring)).mkString(",")
     val ff = varbedExmplPatterns.map(x =>
         s"\n${f(x)},posNotCovered(${x.tostring}):example(${x.tostring})," +
           s" not ${x.tostring};\n${f(x)},negsCovered(${x.tostring}):${x.tostring}," +
           s" not example(${x.tostring})").mkString(";")

     val minimize = Core.glvalues("perfect-fit") match {
         case "true" => "#minimize{1,I,J:use2(I,J) ; 1,I,J,K:use3(I,J,K)}."
         case "false" => "\n#minimize{\n1,I,J:use2(I,J) ; 1,I,J,K:use3(I,J,K) ;" + ff ++ "\n}."
         case _ => throw new RuntimeException("Unspecified parameter for perfect-fit")
     }

     ASP.toASPprogram(
         program =
           examples("annotation") ++
             examples("narrative") ++
             program ++ List(minimize) ++
             List(s"\n#include "+"\""+Core.bkFile+"\".") ++
             List("\n:- use2(I,J), not use2(I,0).\n") ++
             List("\n#show use2/2.\n \n#show use3/3.\n"),
         //constraints = constraints, show = show,
         writeToFile = aspInputFile.getCanonicalPath)

      (defeasibleKS, use2AtomsMap, defeasiblePrior, use3AtomsMap, defeasible_Rule, use3AtomsMap_Rule)
   }




   def getCoverageDirectives(learningTerminatedAtOnly: Boolean = false,
                             withCWA: String = Core.glvalues("cwa"),
                             checkConsistencyOnly: Boolean = false): List[String] = {

      val varbedExmplPatterns = Core.examplePatterns map (x => x.varbed.tostring)
      varbedExmplPatterns.flatMap(x =>
         Core.glvalues("cwa") match {
            // CWA on the examples:
            case "true" =>
               learningTerminatedAtOnly match {
                  case true => List(s":- example($x), not $x.", s":- $x, not example($x).", s"$x :- example($x).")
                  case _ =>
                    if(!checkConsistencyOnly) List(s":- example($x), not $x.", s":- $x, not example($x).")
                    else List(s":- $x, not example($x).")

               }
            // No CWA on the examples, agnostic with missing examples, explicit negatives:
            case _ =>
              //List(s":- example($x), not $x.", s":- $x, negExample($x).", s"$x :- example($x).")
              List(s":- example($x), not $x.", s":- $x, negExample($x).")
               /*
               learningTerminatedAtOnly match {
                  case true => List(s":- example($x), not $x.", s":- $x, negExample($x).", s"$x :- example($x).")
                  case _ =>
                    if(!checkConsistencyOnly) List(s":- example($x), not $x.", s":- $x, not example($x).")
                    else List(s":- $x, not example($x).")

               }
               */
         })
   }

}
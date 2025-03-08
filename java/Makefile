all: run

clean:
	rm -f out/PiEstimate.jar

out/PiEstimate.jar: out/parcs.jar src/PiEstimate.java
	@mkdir -p temp
	@javac -cp out/parcs.jar -d temp src/PiEstimate.java
	@jar cf out/PiEstimate.jar -C temp .
	@rm -rf temp/

build: out/PiEstimate.jar

run: out/PiEstimate.jar
    @cd out && java -cp 'parcs.jar:PiEstimate.jar' PiEstimate $(WORKERS) > ../sorted_output.txt

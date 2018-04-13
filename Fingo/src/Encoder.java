import java.util.ArrayList;
import java.util.HashMap;

public class Encoder {
    private ArrayList<String> dictionary;
    private HashMap<Character, String> numbersDictionary;
    private HashMap<String, String> encodedDictionary;

    Encoder(ArrayList<String> dictionary){
        this.dictionary = dictionary;
        numbersDictionary = fillNumbersMap();
        encodedDictionary = new HashMap<>();
    }

    private HashMap<Character, String> fillNumbersMap() {
        HashMap<Character, String> numbersDictionary = new HashMap();

        numbersDictionary.put('a', "2");
        numbersDictionary.put('b', "22");
        numbersDictionary.put('c', "222");
        numbersDictionary.put('ą', "2222");
        numbersDictionary.put('ć', "22222");

        numbersDictionary.put('d', "3");
        numbersDictionary.put('e', "33");
        numbersDictionary.put('f', "333");
        numbersDictionary.put('ę', "3333");

        numbersDictionary.put('g', "4");
        numbersDictionary.put('h', "44");
        numbersDictionary.put('i', "444");

        numbersDictionary.put('j', "5");
        numbersDictionary.put('k', "55");
        numbersDictionary.put('l', "555");
        numbersDictionary.put('ł', "5555");

        numbersDictionary.put('m', "6");
        numbersDictionary.put('n', "66");
        numbersDictionary.put('o', "666");
        numbersDictionary.put('ń', "6666");
        numbersDictionary.put('ó', "66666");

        numbersDictionary.put('p', "7");
        numbersDictionary.put('q', "77");
        numbersDictionary.put('r', "777");
        numbersDictionary.put('s', "7777");
        numbersDictionary.put('ś', "77777");

        numbersDictionary.put('t', "8");
        numbersDictionary.put('u', "88");
        numbersDictionary.put('v', "888");

        numbersDictionary.put('w', "9");
        numbersDictionary.put('x', "99");
        numbersDictionary.put('y', "999");
        numbersDictionary.put('z', "9999");
        numbersDictionary.put('ź', "99999");
        numbersDictionary.put('ż', "999999");

        return numbersDictionary;
    }

    public HashMap<String, String> getEncodedDictionary() {
        return encodeDictionary();
    }

    private HashMap<String, String> encodeDictionary(){

        for (String word : dictionary) {
            StringBuilder encodedWord = new StringBuilder();
            for(int letterIndex = 0; letterIndex < word.length(); letterIndex++){
                encodedWord.append(numbersDictionary.get(word.charAt(letterIndex)));
            }
            encodedDictionary.put(word, encodedWord.toString());
        }

        return encodedDictionary;
    }
}

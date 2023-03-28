package com.example.encrypter;

public class Home_Conversion {

    protected static String runConversion(String input, int rotor_1, int rotor_2, int rotor_3, boolean decrypt_on, boolean hexadecimal_on) {

        char[] eng_2 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] eng_1 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] eng_big_2 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[] eng_big_1 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        char[] rus_2 = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
        char[] rus_1 = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
        char[] rus_big_2 = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};
        char[] rus_big_1 = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

        char[] arm_2 = {'ա', 'բ', 'գ', 'դ', 'ե', 'զ', 'է', 'ը', 'թ', 'ժ', 'ի', 'լ', 'խ', 'ծ', 'կ', 'հ', 'ձ', 'ղ', 'ճ', 'մ', 'յ', 'ն', 'շ', 'ո', 'չ', 'պ', 'ջ', 'ռ', 'ս', 'վ', 'տ', 'ր', 'ց', 'ւ', 'փ', 'ք', 'օ', 'ֆ'};
        char[] arm_1 = {'ա', 'բ', 'գ', 'դ', 'ե', 'զ', 'է', 'ը', 'թ', 'ժ', 'ի', 'լ', 'խ', 'ծ', 'կ', 'հ', 'ձ', 'ղ', 'ճ', 'մ', 'յ', 'ն', 'շ', 'ո', 'չ', 'պ', 'ջ', 'ռ', 'ս', 'վ', 'տ', 'ր', 'ց', 'ւ', 'փ', 'ք', 'օ', 'ֆ'};
        char[] arm_big_2 = {'Ա', 'Բ', 'Գ', 'Դ', 'Ե', 'Զ', 'Է', 'Ը', 'Թ', 'Ժ', 'Ի', 'Լ', 'Խ', 'Ծ', 'Կ', 'Հ', 'Ձ', 'Ղ', 'Ճ', 'Մ', 'Յ', 'Ն', 'Շ', 'Ո', 'Չ', 'Պ', 'Ջ', 'Ռ', 'Ս', 'Վ', 'Տ', 'Ր', 'Ց', 'Ւ', 'Փ', 'Ք', 'Օ', 'Ֆ'};
        char[] arm_big_1 = {'Ա', 'Բ', 'Գ', 'Դ', 'Ե', 'Զ', 'Է', 'Ը', 'Թ', 'Ժ', 'Ի', 'Լ', 'Խ', 'Ծ', 'Կ', 'Հ', 'Ձ', 'Ղ', 'Ճ', 'Մ', 'Յ', 'Ն', 'Շ', 'Ո', 'Չ', 'Պ', 'Ջ', 'Ռ', 'Ս', 'Վ', 'Տ', 'Ր', 'Ց', 'Ւ', 'Փ', 'Ք', 'Օ', 'Ֆ'};

        char[] symbol_2 = {'`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', '|', ';', ':', '"', ',', '.', '<', '>', '/', '?', '~'};
        char[] symbol_1 = {'`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', '|', ';', ':', '"', ',', '.', '<', '>', '/', '?', '~'};

        char[] number_2 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        char[] number_1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        int i, j;
        char letter;
        String result = "";

        //1st rotor conversion

        //  english conversion
        arrayMover(eng_1, eng_2, rotor_1);
        arrayMover(eng_big_1, eng_big_2, rotor_1);
        // russian conversion
        arrayMover(rus_1, rus_2, rotor_1);
        arrayMover(rus_big_1, rus_big_2, rotor_1);
        // armenian conversion
        arrayMover(arm_1, arm_2, rotor_1);
        arrayMover(arm_big_1, arm_big_2, rotor_1);
        // symbol conversion
        arrayMover(symbol_1, symbol_2, rotor_1);
        // number conversion
        arrayMover(number_1, number_2, rotor_1);

        // 2nd rotor conversion
        letterChanger(eng_2, rotor_2);
        letterChanger(eng_big_2, rotor_2);
        letterChanger(rus_2, rotor_2);
        letterChanger(rus_big_2, rotor_2);
        letterChanger(arm_2, rotor_2);
        letterChanger(arm_big_2, rotor_2);
        letterChanger(symbol_2, rotor_2);
        numberChanger(number_2,rotor_2);

        // 3rd rotor conversion
        if(rotor_3 != -1){
            letterChangerAdvanced(eng_2, rotor_3);
            letterChangerAdvanced(eng_big_2, rotor_3);
            letterChangerAdvanced(rus_2, rotor_3);
            letterChangerAdvanced(rus_big_2, rotor_3);
            letterChangerAdvanced(arm_2, rotor_3);
            letterChangerAdvanced(arm_big_2, rotor_3);
            letterChangerAdvanced(symbol_2, rotor_3);
            numberChangerAdvanced(number_2,rotor_3);
        }

        //check input length
        if(input.length() > 3000) return "* not more than 3000 letters *";

        // processing text data
        if (!decrypt_on) {
            // encryption
            for (i = 0; i < input.length(); i++) {
                letter = input.charAt(i);

                for (j = 0; j < eng_1.length; j++) {
                    if (letter == eng_1[j]) {
                        letter = eng_2[j];
                        break;
                    } else if (letter == eng_big_1[j]) {
                        letter = eng_big_2[j];
                        break;
                    }
                }

                for (j = 0; j < symbol_1.length; j++) {
                    if (letter == symbol_1[j]) {
                        letter = symbol_2[j];
                        break;
                    }
                }
                for (j = 0; j < arm_1.length; j++) {
                    if (letter == arm_1[j]) {
                        letter = arm_2[j];
                        break;
                    } else if (letter == arm_big_1[j]) {
                        letter = arm_big_2[j];
                        break;
                    }
                }
                for (j = 0; j < rus_1.length; j++) {
                    if (letter == rus_1[j]) {
                        letter = rus_2[j];
                        break;
                    } else if (letter == rus_big_1[j]) {
                        letter = rus_big_2[j];
                        break;
                    }
                }
                for (j = 0; j < number_1.length; j++) {
                    if (letter == number_1[j]) {
                        letter = number_2[j];
                        break;
                    }
                }
                result = result + letter;
            }

            if(hexadecimal_on)
                result = hexadecimalEncryption(result,false);
        }

        else {
            // decryption
            if(hexadecimal_on)
                input = hexadecimalEncryption(input,true);
            if(input == null)
                return "* not hexadecimal input *";

            for (i = 0; i < input.length(); i++) {
                letter = input.charAt(i);
                for (j = 0; j < eng_1.length; j++) {
                    if (letter == eng_2[j]) {
                        letter = eng_1[j];
                        break;
                    } else if (letter == eng_big_2[j]) {
                        letter = eng_big_1[j];
                        break;
                    }
                }
                for (j = 0; j < symbol_1.length; j++) {
                    if (letter == symbol_2[j]) {
                        letter = symbol_1[j];
                        break;
                    }
                }
                for (j = 0; j < arm_1.length; j++) {
                    if (letter == arm_2[j]) {
                        letter = arm_1[j];
                        break;
                    } else if (letter == arm_big_2[j]) {
                        letter = arm_big_1[j];
                        break;
                    }
                }
                for (j = 0; j < rus_1.length; j++) {
                    if (letter == rus_2[j]) {
                        letter = rus_1[j];
                        break;
                    } else if (letter == rus_big_2[j]) {
                        letter = rus_big_1[j];
                        break;
                    }
                }
                for (j = 0; j < number_1.length; j++) {
                    if (letter == number_2[j]) {
                        letter = number_1[j];
                        break;
                    }
                }
                result = result + letter;
            }
        }
        return result;
    }

    // swap letters given array and two indexes
    private static void swap(char[] Array_2, int a, int b) {
        char r = Array_2[a];
        Array_2[a] = Array_2[b];
        Array_2[b] = r;
    }

    // move given array by given point
    private static void arrayMover(char[] Array_1, char[] Array_2, int point) {
        int i, j;

        while (point >= Array_1.length) point -= Array_1.length;

        for (i = 0, j = point; i < Array_1.length - point; i++, j++) {
            char r = Array_2[i];
            Array_2[i] = Array_1[j];
            Array_2[j] = r;
        }
        for (i = Array_1.length - point, j = 0; i < Array_1.length; i++, j++) {
            Array_2[i] = Array_1[j];
        }
    }

    // replace letter given array by given point
    private static void letterChanger(char[] Array_2, int point) {
        if (point != 0) {
            if (point % 2 == 0) {
                swap(Array_2, 1, 9);
                swap(Array_2, 2, 11);
                swap(Array_2, 0, 13);
                swap(Array_2, 4, 16);
                swap(Array_2, 7, 19);
                swap(Array_2, 14, 17);
                swap(Array_2, 21, 20);
            } else {
                swap(Array_2, 1, 12);
                swap(Array_2, 3, 6);
                swap(Array_2, 0, 11);
                swap(Array_2, 8, 16);
                swap(Array_2, 2, 23);
                swap(Array_2, 15, 13);
                swap(Array_2, 4, 18);
            }
            if (point % 3 == 0) {
                swap(Array_2, 1, 2);
                swap(Array_2, 4, 15);
                swap(Array_2, 4, 6);
                swap(Array_2, 12, 11);
            } else {
                swap(Array_2, 1, 12);
                swap(Array_2, 3, 6);
                swap(Array_2, 5, 8);
                swap(Array_2, 7, 19);
            }
            if (point % 4 == 0) {
                swap(Array_2, 2, 8);
                swap(Array_2, 03, 17);
                swap(Array_2, 6, 14);
                swap(Array_2, 5, 7);
            } else {
                swap(Array_2, 4, 6);
                swap(Array_2, 5, 12);
                swap(Array_2, 1, 9);
                swap(Array_2, 7, 10);
            }
            if (point % 5 == 0) {
                swap(Array_2, 1, 3);
                swap(Array_2, 7, 17);
                swap(Array_2, 18, 20);
                swap(Array_2, 12, 14);
            } else {
                swap(Array_2, 2, 24);
                swap(Array_2, 7, 8);
                swap(Array_2, 0, 17);
                swap(Array_2, 13, 19);
            }
        }

    }

    // replace letter given array by given point (advanced)
    private static void letterChangerAdvanced(char[] Array_2, int point) {
        if (point != 0) {
            if (point % 2 == 0) {
                swap(Array_2, 0, 9);
                swap(Array_2, 2, 11);
                swap(Array_2, 11, 13);
                swap(Array_2, 5, 16);
                swap(Array_2, 7, 19);
                swap(Array_2, 1, 17);
                swap(Array_2, 4, 20);
            } else {
                swap(Array_2, 0, 12);
                swap(Array_2, 2, 6);
                swap(Array_2, 5, 11);
                swap(Array_2, 0, 16);
                swap(Array_2, 3, 23);
                swap(Array_2, 13, 13);
                swap(Array_2, 6, 18);
            }
            if (point % 3 == 0) {
                swap(Array_2, 17, 2);
                swap(Array_2, 0, 15);
                swap(Array_2, 2, 6);
                swap(Array_2, 12, 11);
            } else {
                swap(Array_2, 2, 12);
                swap(Array_2, 5, 6);
                swap(Array_2, 4, 8);
                swap(Array_2, 1, 19);
            }
            if (point % 4 == 0) {
                swap(Array_2, 6, 8);
                swap(Array_2, 14, 17);
                swap(Array_2, 3, 14);
                swap(Array_2, 5, 7);
            } else {
                swap(Array_2, 5, 6);
                swap(Array_2, 4, 12);
                swap(Array_2, 10, 9);
                swap(Array_2, 9, 10);
            }
            if (point % 5 == 0) {
                swap(Array_2, 3, 3);
                swap(Array_2, 0, 17);
                swap(Array_2, 8, 20);
                swap(Array_2, 1, 14);
            } else {
                swap(Array_2, 2, 24);
                swap(Array_2, 5, 8);
                swap(Array_2, 4, 17);
                swap(Array_2, 12, 19);
            }
        }

    }

    // replace number given array by given point
    private static void numberChanger(char[] Array_2, int point){
        if (point != 0) {
            if (point % 2 == 0) {
                swap(Array_2, 0, 3);
                swap(Array_2, 2, 4);
                swap(Array_2, 6, 9);
                swap(Array_2, 7, 1);
            } else {
                swap(Array_2, 7, 8);
                swap(Array_2, 0, 6);
                swap(Array_2, 5, 4);
                swap(Array_2, 1, 2);
            }
            if (point % 3 == 0) {
                swap(Array_2, 8, 7);
                swap(Array_2, 5, 2);
            } else {
                swap(Array_2, 0, 6);
                swap(Array_2, 3, 4);
            }
            if (point % 4 == 0) {
                swap(Array_2, 1, 5);
                swap(Array_2, 4, 0);
            } else {
                swap(Array_2, 2, 9);
                swap(Array_2, 3, 4);
            }
            if (point % 5 == 0) {
                swap(Array_2, 5, 7);
                swap(Array_2, 0, 8);
            } else {
                swap(Array_2, 8, 3);
                swap(Array_2, 2, 5);
            }
        }
    }

    // replace number given array by given point (advanced)
    private static void numberChangerAdvanced(char[] Array_2, int point){
        if (point != 0) {
            if (point % 2 == 0) {
                swap(Array_2, 4, 7);
                swap(Array_2, 1, 5);
                swap(Array_2, 3, 9);
                swap(Array_2, 0, 6);
            } else {
                swap(Array_2, 1, 2);
                swap(Array_2, 4, 3);
                swap(Array_2, 6, 0);
                swap(Array_2, 5, 8);
            }
            if (point % 3 == 0) {
                swap(Array_2, 1, 2);
                swap(Array_2, 3, 4);
            } else {
                swap(Array_2, 6, 3);
                swap(Array_2, 1, 0);
            }
            if (point % 4 == 0) {
                swap(Array_2, 7, 8);
                swap(Array_2, 5, 2);
            } else {
                swap(Array_2, 4, 3);
                swap(Array_2, 0, 4);
            }
            if (point % 5 == 0) {
                swap(Array_2, 1, 9);
                swap(Array_2, 4, 7);
            } else {
                swap(Array_2, 5, 8);
                swap(Array_2, 3, 1);
            }
        }
    }

    // 16 base (hexadecimal) conversion given input and state
    private static String hexadecimalEncryption(String input, boolean decrypt_on){
        String result = "";
        String r = "";

        if(!decrypt_on){
            for(int i = 0; i < input.length(); i++){
                int x = input.charAt(i);
                r = Integer.toHexString(x);
                if(i != input.length() - 1) result = result + r + "-";
                else result = result + r;
            }
        }
        else{
            try{
                int k = 0;
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) == '-') k++;
                }

                int length = input.length() - (k * 2 + 1);
                for (int i = 0; i < length; i++) {
                    for (int j = 0; j < input.length(); j++) {
                        if (input.charAt(j) != '-') r = r + input.charAt(j);
                        else {
                            input = input.substring(j + 1);
                            break;
                        }
                    }
                    int x = Integer.parseInt(r, 16);
                    r = "";
                    char a = (char) x;
                    result = result + a;
                }
            }
            catch(Exception e){
                return null;
            }
        }
        return result;
    }

}

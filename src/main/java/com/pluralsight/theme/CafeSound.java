package com.pluralsight.theme;

// Java's built-in sound tools (javax.sound.midi)
import javax.sound.midi.MidiChannel;   // a channel = one instrument slot (2 channels = 2 instruments at once)
import javax.sound.midi.MidiSystem;    // Java's sound department: it hands us the keyboard
import javax.sound.midi.Synthesizer;   // the keyboard itself: it makes all the sound

/**
 * CafeSound
 * All the cafe sounds in one place: a dreamy welcome when the app starts,
 * a deposit sound (notes rise) and a payment sound (notes fall).
 * No main() here: other classes just call CafeSound.playDreamyWelcome() etc.
 */
public class CafeSound {

    // * On/off switch: set to false while testing so you don't hear it every run
    public static boolean soundOn = true;

    // ===== Welcome: a soft, dreamy chord with a quiet pad underneath (about 2.5 s) =====
    public static void playDreamyWelcome() {
        if (!soundOn) {
            return;   // sound is switched off: skip it
        }

        int[] chord = {53, 57, 60, 64};   // ONE chord made of 4 notes: warm, low and mellow

        try {
            // * Get the keyboard and turn it on
            Synthesizer synth = MidiSystem.getSynthesizer();   // ask Java for its keyboard, call it synth
            synth.open();                                      // plug it in / turn it on

            // * Two "hands" = two slots on the keyboard (an array of 16 slots, index 0-15)
            MidiChannel piano = synth.getChannels()[0];         // hand 1 = slot 0
            MidiChannel pad = synth.getChannels()[1];           // hand 2 = slot 1
            // ? Any slot works ([4], [5]...): the number is just WHICH slot, not an instrument
            // ! Except slot 9: it's reserved for drums

            // * Pick each hand's instrument (numbers from the standard General MIDI list, 0-127)
            piano.programChange(4);   // 4 = electric piano: soft and warm
            pad.programChange(89);    // 89 = warm pad: a soft cloud underneath

            // noteOn(which key, how hard): 60 = middle C, press strength 0-127 (lower = softer)
            pad.noteOn(41, 30);       // one quiet low note, held the whole time (the background hum)

            // * Roll the chord: press each note a tiny bit after the last one
            // ! noteOn = press AND HOLD. Leaving the loop does NOT let go, so the notes overlap and keep ringing
            for (int note : chord) {
                piano.noteOn(note, 40);   // soft press, stays held
                Thread.sleep(90);         // tiny gap = the dreamy "roll"
            }

            // ? Thread.sleep only makes the CODE wait. The synth keeps the sound going by itself
            Thread.sleep(500);        // the chord rings on its own
            piano.noteOn(69, 32);     // one soft high note joins the ringing chord, like a sigh
            Thread.sleep(1200);       // all 5 notes ring together

            // * Let go, fade, turn off
            piano.allNotesOff();      // lift the piano hand (the sound starts fading)
            pad.allNotesOff();        // lift the pad hand
            Thread.sleep(700);        // ! let the tail fade naturally, closing now would cut it off
            synth.close();            // unplug the whole keyboard

        } catch (Exception e) {
            // no sound available (no speakers, sound blocked): skip it instead of crashing
            System.out.println("(sound unavailable)");
        }
    }

    // ===== Deposit: money in, the notes rise up (about 1 s) =====
    public static void playDepositSound() {
        playSoftNotes(new int[]{60, 64, 67});   // C -> E -> G (up = happy)
    }

    // ===== Payment: money out, the same notes float down (about 1 s) =====
    public static void playPaymentSound() {
        playSoftNotes(new int[]{67, 64, 60});   // G -> E -> C (down = settling)
    }

    // * Shared helper: plays ANY notes softly on the electric piano
    // ? Deposit and payment both reuse it, they just pass different notes (write once, reuse)
    public static void playSoftNotes(int[] notes) {
        if (!soundOn) {
            return;   // sound is switched off: skip it
        }

        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel piano = synth.getChannels()[0];   // one hand is enough here
            piano.programChange(4);                       // the same warm electric piano as the welcome

            for (int note : notes) {
                piano.noteOn(note, 38);      // soft press, stays held
                Thread.sleep(110);           // a gentle little roll
            }

            Thread.sleep(450);               // let the notes ring softly
            piano.allNotesOff();             // let go
            Thread.sleep(150);               // short fade
            synth.close();                   // turn the keyboard off

        } catch (Exception e) {
            System.out.println("(sound unavailable)");
        }
    }
}
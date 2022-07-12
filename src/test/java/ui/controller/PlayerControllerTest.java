package ui.controller;

import javax.inject.Inject;

import lombok.var;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;

public class PlayerControllerTest {

    @Mock
    private PlayerController playerController;

    @Test
    public void test(){
        var list = playerController.fighter.getItems();
        assertTrue(list.size()>0);
    }

}

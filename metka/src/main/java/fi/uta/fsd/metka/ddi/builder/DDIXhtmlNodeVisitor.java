package fi.uta.fsd.metka.ddi.builder;

import codebook25.SimpleTextType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.w3.x1999.xhtml.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by henrisu on 30.1.2017.
 */
public class DDIXhtmlNodeVisitor implements NodeVisitor {

    private SimpleTextType stt;
    private XmlObject cur;

    public DDIXhtmlNodeVisitor(SimpleTextType stt) {
        this.stt = stt;
        this.cur = stt;
    }

    @Override
    public void head(Node node, int i) {
        System.out.println("at head, depth " + i + ", node tag: " + node.nodeName() + ", node: " + node.toString() );
        switch (node.nodeName()) {
            case "h1":
                insertH1();
                break;
            case "h2":
                insertH2();
                break;
            case "h3":
                insertH3();
                break;
            case "h4":
                insertH4();
                break;
            case "h5":
                insertH5();
                break;
            case "h6":
                insertH6();
                break;
            case "ul":
                insertUl();
                break;
            case "ol":
                insertOl();
                break;
            case "dl":
                insertDl();
                break;
            case "p":
                insertP();
                break;
            case "div":
                break;
            case "pre":
                break;
            case "blockquote":
                break;
            case "address":
                break;
            case "hr":
                break;
            case "table":
                break;
            case "br":
                insertBr();
                break;
            case "#text":
                appendXML(node.toString());
                break;
            default:
                break;
        }
    }

    private void insertH1() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewH1();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewH1();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewH1();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewH1();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewH1();
        }
    }

    private void insertH2() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewH2();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewH2();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewH2();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewH2();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewH2();
        }
    }

    private void insertH3() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewH3();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewH3();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewH3();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewH3();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewH3();
        }
    }

    private void insertH4() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewH4();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewH4();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewH4();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewH4();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewH4();
        }
    }

    private void insertH5() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewH5();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewH5();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewH5();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewH5();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewH5();
        }
    }

    private void insertH6() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewH6();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewH6();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewH6();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewH6();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewH6();
        }
    }

    private void insertUl() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewUl();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewUl();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewUl();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewUl();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewUl();
        }
    }

    private void insertOl() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewOl();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewOl();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewOl();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewOl();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewOl();
        }
    }

    private void insertDl() {
        //div, li, td, th

        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewDl();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewDl();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewDl();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewDl();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewDl();
        }
    }

    private void insertP() {
        if (this.cur instanceof SimpleTextType) {
            this.cur = ((SimpleTextType) this.cur).addNewP2();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewP();
        } else if (this.cur instanceof LiType) {
            this.cur = ((LiType) this.cur).addNewP();
        } else if (this.cur instanceof TdType) {
            this.cur = ((TdType) this.cur).addNewP();
        } else if (this.cur instanceof ThType) {
            this.cur = ((ThType) this.cur).addNewP();
        }
    }


    private void insertBr() {
        if (this.cur instanceof PType) {
            this.cur = ((PType) this.cur).addNewBr();
        } else if (this.cur instanceof DivType) {
            this.cur = ((DivType) this.cur).addNewBr();
        }
            /*switch (this.cur.getClass().getSimpleName()) {
                case "PTypeImpl":

                default:
                    break;

            }*/
    }

    private void appendXML(String value) {
        XmlCursor cursor = cur.newCursor();
        cursor.setTextValue(value);
        cursor.dispose();
    }
    @Override
    public void tail(Node node, int i) {
        System.out.println("at tail, depth " + i + ", node tag: " + node.nodeName() + ", node: " + node.toString() );
    }
}

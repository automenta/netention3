package netention.test;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import netention.MemCommunity;
import netention.NType;
import netention.Property;
import netention.value.integer.IntProp;
import netention.value.node.NodeProp;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;


//import gnu.getopt.LongOpt;
//import gnu.getopt.Getopt;

/* 
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * <p>Simple example. Read an ontology, and display the class hierarchy. May use a
 * reasoner to calculate the hierarchy.</p>
 * 
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 17-03-2007<br>
 * <br>
 */
public class SimpleHierarchyExample {
    private static int INDENT = 4;

    private OWLReasonerFactory reasonerFactory;

    private OWLOntology ontology;

    private PrintStream out;

    static MemCommunity comm;
    
    public SimpleHierarchyExample(OWLOntologyManager manager, OWLReasonerFactory reasonerFactory)
            throws OWLException, MalformedURLException {
        this.reasonerFactory = reasonerFactory;
        out = System.out;
    }

    public static Set<String> getTypeSet(Set<? extends OWLClassExpression> classes) {
        Set<String> ss = new HashSet();
        for (OWLClassExpression ec : classes) {
            ss.add(ec.asOWLClass().getIRI().toString());
        }
        return Collections.unmodifiableSet( ss );
    }
    
    /**
     * Print the class hierarchy for the given ontology from this class down, assuming this class is at
     * the given level. Makes no attempt to deal sensibly with multiple
     * inheritance.
     */
    public void printHierarchy(OWLOntology ontology, OWLClass clazz) throws OWLException {
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
        this.ontology = ontology;

        
        System.out.println("PRINT DATATYPES");
        
        for (OWLDataProperty dp : ontology.getDataPropertiesInSignature()) {
            final String iri = dp.getIRI().toString();
            
            System.out.println(iri + " " +  dp.getDomains(ontology) + " " + dp.getRanges(ontology));
            System.out.println("  " + dp.getDatatypesInSignature());
            
            Property p = comm.getPropertyByID(iri);
            if (p == null) {
                Set<OWLDataRange> ranges = dp.getRanges(ontology);
                for (OWLDataRange o : ranges) {
                    String t = o.toString();
                    if (t.equals("xsd:integer")) {
                        p = new IntProp(iri, iri);
                    }
                    
                }
                
                if (p!=null) {
                    p.setDomains(getTypeSet(dp.getDomains(ontology)));
 
                    //TODO fold with bottom annotation iteration into a common method
                    for (final OWLAnnotation a : dp.getAnnotations(ontology)) {
                        final String airi = a.getProperty().getIRI().toString();
                        if (airi.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
                            p.setDescription( a.getValue().toString() );
                        }
                        else if (airi.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                            p.setName( a.getValue().toString() );
                        }
                    }
                    comm.save(p);
                }
                
            }
        }
        
        //System.out.println(ontology.getDatatypesInSignature());

        System.out.println("PRINT OBJECTTYPES");
        for (OWLObjectProperty op : ontology.getObjectPropertiesInSignature()) {
            final String iri = op.getIRI().toString();
            
            System.out.println("  " + iri + " " +  op.getDomains(ontology) + " " + op.getRanges(ontology));
            Property p = comm.getPropertyByID(iri);
            if (p == null) {
                p = new NodeProp(iri, iri, getTypeSet(op.getDomains(ontology)), getTypeSet(op.getRanges(ontology)));
                p.setName( op.getIRI().getFragment().toString() );
                
                for (final OWLAnnotation a : op.getAnnotations(ontology)) {
                    final String airi = a.getProperty().getIRI().toString();
                    if (airi.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
                        p.setDescription( a.getValue().toString() );
                    }
                    else if (airi.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                        p.setName( a.getValue().toString() );
                    }
                }

                comm.save(p);
            }
        }
        
        System.out.println("PRINT CLASSES");
        printHierarchy(reasoner, clazz, 0 );
        
        for (OWLClass cl: ontology.getClassesInSignature()) {
            if (!reasoner.isSatisfiable(cl)) {
                System.out.println("unsatisfied: " + cl.getIRI());
            }
        }
        
        comm.print();
        
        
        reasoner.dispose();
    }
    
    
    /**
     * Print the class hierarchy from this class down, assuming this class is at
     * the given level. Makes no attempt to deal sensibly with multiple
     * inheritance.
     */
    public void printHierarchy(OWLReasoner reasoner, OWLClass clazz, int level)
            throws OWLException {
        /*
         * Only print satisfiable classes -- otherwise we end up with bottom
         * everywhere
         */
        if (clazz.isAnonymous())
                return;
        if (reasoner.isSatisfiable(clazz)) {

            for (int i = 0; i < level * INDENT; i++) {
                out.print(" ");
            }
            System.out.println(clazz.getIRI() + " " + clazz.getSuperClasses(ontology) + " " + reasoner.getSuperClasses(clazz, true));
                
            
            NType t = comm.getType(clazz.getIRI().getFragment());
            if (t == null) {
                try {
                    t = comm.newType(clazz.getIRI().toString());
                    t.setName( clazz.getIRI().getFragment() );
                    
                    for (final OWLAnnotation a : clazz.getAnnotations(ontology)) {
                        final String airi = a.getProperty().getIRI().toString();
                        if (airi.equals("http://www.w3.org/2000/01/rdf-schema#comment")) {
                            t.description = a.getValue().toString();
                        }
                        else if (airi.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
                            t.setName( a.getValue().toString() );
                        }
                    }
                    
                    for (OWLClassExpression p : clazz.getSuperClasses(ontology)) {
                        if (p.isAnonymous())
                            continue;
                        t.superTypes.add(p.asOWLClass().getIRI().toString());
                    }
                    comm.save(t);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            
            //out.println(labelFor( clazz ));
            /* Find the children and recurse */
                for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
                    if (!child.equals(clazz)) {
                        printHierarchy(reasoner, child, level + 1);
                    }
                }
        }
    }

    public static void main(String[] args) {
        
        try {
           comm = new MemCommunity();
                     
           String reasonerFactoryClassName = "StructuralReasonerFactory";

           OWLOntologyManager manager = OWLManager.createOWLOntologyManager();


           // Now load the ontology.
           OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("/tmp/x.owl"));

           // Report information about the ontology
           System.out.println("Ontology Loaded...");
           //System.out.println("Document IRI: " + documentIRI);
           System.out.println("Ontology : " + ontology.getOntologyID());
           System.out.println("Format      : " + manager.getOntologyFormat(ontology));

           // / Create a new SimpleHierarchy object with the given reasoner.
           SimpleHierarchyExample simpleHierarchy = new SimpleHierarchyExample(manager, new StructuralReasonerFactory());

           OWLClass clazz = manager.getOWLDataFactory().getOWLClass(OWLRDFVocabulary.OWL_THING.getIRI());

           simpleHierarchy.printHierarchy(ontology, clazz );


           comm.saveJSON("/tmp/netention.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

import { Button } from "@/components/ui/button";
import Link from "next/link";

export default async function Home() {
    return (
        <div>
            <Button asChild>
                <Link href={"/products"} prefetch>
                    Go to products page
                </Link>
            </Button>
        </div>
    );
}
